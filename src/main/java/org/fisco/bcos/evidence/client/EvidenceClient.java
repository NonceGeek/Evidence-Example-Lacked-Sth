package org.fisco.bcos.evidence.client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream; //plus
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Properties;
import org.fisco.bcos.evidence.contract.EvidenceFactory;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;



import java.util.ArrayList; // ArrayList
import java.util.Arrays; //Array

public class EvidenceClient {

  static Logger logger = LoggerFactory.getLogger(EvidenceClient.class);

  private BcosSDK bcosSDK;
  private Client client;
  private CryptoKeyPair cryptoKeyPair;

  public void initialize() throws Exception {
    @SuppressWarnings("resource")
    ApplicationContext context =
        new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    bcosSDK = context.getBean(BcosSDK.class);
    client = bcosSDK.getClient(1);

    String privkey = loadPrivKey();

    cryptoKeyPair = client.getCryptoSuite().createKeyPair(privkey);
    client.getCryptoSuite().setCryptoKeyPair(cryptoKeyPair);
    logger.debug("create client for group1, account address is " + cryptoKeyPair.getAddress());
  }

  public String loadPrivKey() throws Exception {
    Properties prop = new Properties();
    final Resource contractResource = new ClassPathResource("contract.properties");
    prop.load(contractResource.getInputStream());
    String privkey = prop.getProperty("priv_key");
    if (privkey == null) {
      throw new Exception(" priv key is not exist in the properties. ");
    }
    logger.info(" load priv key success", privkey);
    return privkey;
  }

  public void deployEviFacAndRecordAddr(String addr_list_raw) {
    try {
      List<String> addr_list = new ArrayList<String>(Arrays.asList(addr_list_raw.split(",")));
      EvidenceFactory eviFac = EvidenceFactory.deploy(client, cryptoKeyPair, addr_list);
      System.out.println(
          " deploy Evidence success, contract address is " + eviFac.getContractAddress());

      recordEviFacAddr(eviFac.getContractAddress(), addr_list_raw);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      // e.printStackTrace();
      System.out.println(" deploy Evidence contract failed, error message is  " + e.getMessage());
    }
  }

  // update
  public void recordEviFacAddr(String address, String signers) throws FileNotFoundException, IOException {
    Properties prop = new Properties();

    prop.load(new FileInputStream("conf/contract.properties")); // 追加写入

    prop.setProperty("address", address);
    prop.setProperty("signers", signers);

    prop.store(new FileOutputStream("conf/contract.properties"),"priv/contract/signers");
  }

  public String loadEviAddr() throws Exception {
    // load Asset contact address from contract.properties
    Properties prop = new Properties();
    final Resource contractResource = new ClassPathResource("contract.properties");
    prop.load(contractResource.getInputStream());

    String contractAddress = prop.getProperty("address");
    if (contractAddress == null || contractAddress.trim().equals("")) {
      throw new Exception(" load Evi contract address failed, please deploy it first. ");
    }
    logger.info(" load Asset Evi from contract.properties, address is {}", contractAddress);
    return contractAddress;
  }

  public void getSigners(){
    try {
      String contractAddress = loadEviAddr();
      EvidenceFactory evi = EvidenceFactory.load(contractAddress, client, cryptoKeyPair);
      List signers = evi.getSigners();
      System.out.printf(" signers are %s \n", signers);
    }  catch (Exception e) {
      System.out.println(" getSigners failed, error message is  " + e.getMessage());
    }
  }

  public void newEvidence(String evidence){
    try {
      String contractAddress = loadEviAddr();
      EvidenceFactory evi = EvidenceFactory.load(contractAddress, client, cryptoKeyPair);
      TransactionReceipt receipt = evi.newEvidence(evidence);
      List<EvidenceFactory.NewEvidenceEventEventResponse> responses = evi.getNewEvidenceEventEvents(receipt);
      if (!responses.isEmpty()) {
        System.out.printf("key of evidence %s is %s \n", evidence, responses.get(0).addr);
      } else {
        System.out.println(" event log not found, maybe transaction not exec. ");
      }
    }  catch (Exception e) {
      System.out.println(" newEvidence failed, error message is  " + e.getMessage());
    }
  }

  public void getEvidence(String key){
    try{
      String contractAddress = loadEviAddr();
      EvidenceFactory evi = EvidenceFactory.load(contractAddress, client, cryptoKeyPair);
      Tuple3<String, List<String>, List<String>> result = evi.getEvidence(key);
      System.out.printf("evidence value is %s\n", result.getValue1());
      System.out.printf("evidence signers are %s\n", result.getValue2());
      System.out.printf("evidence signed singers are %s\n", result.getValue3());

    }  catch (Exception e) {
      System.out.println("getEvidence failed, error message is  " + e.getMessage());
    }
  }

  public void addSignatures(String privkey, String key){
    try{
      String contractAddress = loadEviAddr();
      CryptoKeyPair tx_sender = client.getCryptoSuite().createKeyPair(privkey);
      EvidenceFactory evi = EvidenceFactory.load(contractAddress, client, tx_sender);
      TransactionReceipt receipt = evi.addSignatures(key);
      Tuple1<Boolean> result = evi.getAddSignaturesOutput(receipt);
      String payload = "";
      if (result.getValue1() == true) {
        payload = "success";
      }
      System.out.printf("addSignature is %s", payload);
    } catch(Exception e){
      System.out.println("addSignatures failed, error message is  " + e.getMessage());
    }
  }

  public static void Usage() {
    System.out.println(" Usage:");
    System.out.println(
        "\t java -cp conf/:lib/*:apps/* org.fisco.bcos.evidence.client.AssetClient deploy");
    System.out.println(
        "\t java -cp conf/:lib/*:apps/* org.fisco.bcos.evidence.client.AssetClient query account");
    System.out.println(
        "\t java -cp conf/:lib/*:apps/* org.fisco.bcos.evidence.client.AssetClient register account value");
    System.out.println(
        "\t java -cp conf/:lib/*:apps/* org.fisco.bcos.evidence.client.AssetClient transfer from_account to_account amount");
    System.exit(0);
  }

  // 关键函数复现
  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      Usage();
    }

    EvidenceClient client = new EvidenceClient();
    client.initialize();

    switch (args[0]) {
      case "deploy":
        client.deployEviFacAndRecordAddr(args[1]);
        break;
      case "getSigners":
        client.getSigners();
        break;
      case "newEvidence":
        client.newEvidence(args[1]);
        break;
      case "getEvidence":
        client.getEvidence(args[1]);
        break;
      case "addSignatures":
        client.addSignatures(args[1], args[2]);
        break;
    }

    System.exit(0);
  }
}
