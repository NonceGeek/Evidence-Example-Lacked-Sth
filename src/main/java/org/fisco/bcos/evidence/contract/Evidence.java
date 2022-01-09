package org.fisco.bcos.evidence.contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionEncoder;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Bool;
import org.fisco.bcos.sdk.abi.datatypes.DynamicArray;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.eventsub.EventCallback;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class Evidence extends Contract {
    public static final String[] BINARY_ARRAY = {"60806040523480156200001157600080fd5b50604051620011ea380380620011ea833981018060405281019080805182019291906020018051906020019092919050505080600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506200009e32620002d8640100000000026401000000009004565b15620001fc578160009080519060200190620000bc929190620003dc565b5060013290806001815401808255809150509060018203906000526020600020016000909192909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550507f68a6f2dd22a3385ea4d4fd8b590e4890ff82d99853226c67df889e74dda33e67828260405180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b83811015620001ba5780820151818401526020810190506200019d565b50505050905090810190601f168015620001e85780820380516001836020036101000a031916815260200191505b50935050505060405180910390a1620002d0565b7f5fab2506db5bd68c3de713fb73321249ea57e4130d6fb55c532f61edc6727ab7828260405180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b838110156200029357808201518184015260208101905062000276565b50505050905090810190601f168015620002c15780820380516001836020036101000a031916815260200191505b50935050505060405180910390a15b50506200048b565b6000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166363a9c3d7836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b1580156200039857600080fd5b505af1158015620003ad573d6000803e3d6000fd5b505050506040513d6020811015620003c457600080fd5b81019080805190602001909291905050509050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200041f57805160ff191683800117855562000450565b8280016001018555821562000450579182015b828111156200044f57825182559160200191906001019062000432565b5b5090506200045f919062000463565b5090565b6200048891905b80821115620004845760008160009055506001016200046a565b5090565b90565b610d4f806200049b6000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063596f21f81461007257806369e8094b1461019257806394cf795e146101c1578063963671be1461022d578063dc58ab1114610284575b600080fd5b34801561007e57600080fd5b506100876102df565b60405180806020018060200180602001848103845287818151815260200191508051906020019080838360005b838110156100cf5780820151818401526020810190506100b4565b50505050905090810190601f1680156100fc5780820380516001836020036101000a031916815260200191505b50848103835286818151815260200191508051906020019060200280838360005b8381101561013857808201518184015260208101905061011d565b50505050905001848103825285818151815260200191508051906020019060200280838360005b8381101561017a57808201518184015260208101905061015f565b50505050905001965050505050505060405180910390f35b34801561019e57600080fd5b506101a7610646565b604051808215151515815260200191505060405180910390f35b3480156101cd57600080fd5b506101d66109c6565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b838110156102195780820151818401526020810190506101fe565b505050509050019250505060405180910390f35b34801561023957600080fd5b50610242610bfc565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561029057600080fd5b506102c5600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610c22565b604051808215151515815260200191505060405180910390f35b6060806060600060606000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663fa69efbd6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561037057600080fd5b505af1158015610384573d6000803e3d6000fd5b505050506040513d602081101561039a57600080fd5b81019080805190602001909291905050509250826040519080825280602002602001820160405280156103dc5781602001602082028038833980820191505090505b509150600090505b8281101561050f57600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16633ffefe4e826040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b15801561047d57600080fd5b505af1158015610491573d6000803e3d6000fd5b505050506040513d60208110156104a757600080fd5b810190808051906020019092919050505082828151811015156104c657fe5b9060200190602002019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff168152505080806001019150506103e4565b6000826001828054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105a95780601f1061057e576101008083540402835291602001916105a9565b820191906000526020600020905b81548152906001019060200180831161058c57829003601f168201915b505050505092508080548060200260200160405190810160405280929190818152602001828054801561063157602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190600101908083116105e7575b50505050509050955095509550505050909192565b600080600090505b6001805490508110156107985760018181548110151561066a57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff16141561078b577ffcf2bd202c015cb2348d6064af1fe7a3dc404134b63f8950d16e0f52399b06ae600060405180806020018281038252838181546001816001161561010002031660029004815260200191508054600181600116156101000203166002900480156107745780601f1061074957610100808354040283529160200191610774565b820191906000526020600020905b81548152906001019060200180831161075757829003601f168201915b50509250505060405180910390a1600191506109c2565b808060010191505061064e565b6107a132610c22565b156108cf5760013290806001815401808255809150509060018203906000526020600020016000909192909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550507f20816d1858bab038efc3fee7f47dbb0de0e5dc3425549996d36d148220973e02600060405180806020018281038252838181546001816001161561010002031660029004815260200191508054600181600116156101000203166002900480156108b85780601f1061088d576101008083540402835291602001916108b8565b820191906000526020600020905b81548152906001019060200180831161089b57829003601f168201915b50509250505060405180910390a1600191506109c2565b7f4263d1d87ff73aebbce6fa4c04cd5b0a3cfed5c33e8a4fac06979f94842046f260003260405180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281038252848181546001816001161561010002031660029004815260200191508054600181600116156101000203166002900480156109ae5780601f10610983576101008083540402835291602001916109ae565b820191906000526020600020905b81548152906001019060200180831161099157829003601f168201915b5050935050505060405180910390a1600091505b5090565b6060600060606000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663fa69efbd6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b158015610a5457600080fd5b505af1158015610a68573d6000803e3d6000fd5b505050506040513d6020811015610a7e57600080fd5b8101908080519060200190929190505050925082604051908082528060200260200182016040528015610ac05781602001602082028038833980820191505090505b509150600090505b82811015610bf357600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16633ffefe4e826040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b158015610b6157600080fd5b505af1","158015610b75573d6000803e3d6000fd5b505050506040513d6020811015610b8b57600080fd5b81019080805190602001909291905050508282815181101515610baa57fe5b9060200190602002019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff16815250508080600101915050610ac8565b81935050505090565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166363a9c3d7836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b158015610ce157600080fd5b505af1158015610cf5573d6000803e3d6000fd5b505050506040513d6020811015610d0b57600080fd5b810190808051906020019092919050505090509190505600a165627a7a72305820901dbe0a8dd7b66836e9ceb78fa4523520ad6ef618208524a08f31a182ddfcee0029"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"60806040523480156200001157600080fd5b50604051620011ea380380620011ea833981018060405281019080805182019291906020018051906020019092919050505080600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506200009e32620002d8640100000000026401000000009004565b15620001fc578160009080519060200190620000bc929190620003dc565b5060013290806001815401808255809150509060018203906000526020600020016000909192909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550507f8365629f789dc2b3893eb80b70568394bdea7d099de648f65f96c2639292355d828260405180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b83811015620001ba5780820151818401526020810190506200019d565b50505050905090810190601f168015620001e85780820380516001836020036101000a031916815260200191505b50935050505060405180910390a1620002d0565b7f6708bcb1180266dd5b7bf47518b5e7c0e3cd3925c8e617b18e71e905f4d40fa3828260405180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b838110156200029357808201518184015260208101905062000276565b50505050905090810190601f168015620002c15780820380516001836020036101000a031916815260200191505b50935050505060405180910390a15b50506200048b565b6000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16638dc858bc836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b1580156200039857600080fd5b505af1158015620003ad573d6000803e3d6000fd5b505050506040513d6020811015620003c457600080fd5b81019080805190602001909291905050509050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200041f57805160ff191683800117855562000450565b8280016001018555821562000450579182015b828111156200044f57825182559160200191906001019062000432565b5b5090506200045f919062000463565b5090565b6200048891905b80821115620004845760008160009055506001016200046a565b5090565b90565b610d4f806200049b6000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063164089ab1461007257806319cd5714146100c95780634ae70cef146101355780635a61c629146102555780637071ea7a146102b0575b600080fd5b34801561007e57600080fd5b506100876102df565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156100d557600080fd5b506100de610305565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b83811015610121578082015181840152602081019050610106565b505050509050019250505060405180910390f35b34801561014157600080fd5b5061014a61053b565b60405180806020018060200180602001848103845287818151815260200191508051906020019080838360005b83811015610192578082015181840152602081019050610177565b50505050905090810190601f1680156101bf5780820380516001836020036101000a031916815260200191505b50848103835286818151815260200191508051906020019060200280838360005b838110156101fb5780820151818401526020810190506101e0565b50505050905001848103825285818151815260200191508051906020019060200280838360005b8381101561023d578082015181840152602081019050610222565b50505050905001965050505050505060405180910390f35b34801561026157600080fd5b50610296600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506108a2565b604051808215151515815260200191505060405180910390f35b3480156102bc57600080fd5b506102c56109a3565b604051808215151515815260200191505060405180910390f35b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6060600060606000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166327f081a16040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561039357600080fd5b505af11580156103a7573d6000803e3d6000fd5b505050506040513d60208110156103bd57600080fd5b81019080805190602001909291905050509250826040519080825280602002602001820160405280156103ff5781602001602082028038833980820191505090505b509150600090505b8281101561053257600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663135fa431826040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b1580156104a057600080fd5b505af11580156104b4573d6000803e3d6000fd5b505050506040513d60208110156104ca57600080fd5b810190808051906020019092919050505082828151811015156104e957fe5b9060200190602002019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff16815250508080600101915050610407565b81935050505090565b6060806060600060606000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166327f081a16040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1580156105cc57600080fd5b505af11580156105e0573d6000803e3d6000fd5b505050506040513d60208110156105f657600080fd5b81019080805190602001909291905050509250826040519080825280602002602001820160405280156106385781602001602082028038833980820191505090505b509150600090505b8281101561076b57600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663135fa431826040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b1580156106d957600080fd5b505af11580156106ed573d6000803e3d6000fd5b505050506040513d602081101561070357600080fd5b8101908080519060200190929190505050828281518110151561072257fe5b9060200190602002019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff16815250508080600101915050610640565b6000826001828054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108055780601f106107da57610100808354040283529160200191610805565b820191906000526020600020905b8154815290600101906020018083116107e857829003601f168201915b505050505092508080548060200260200160405190810160405280929190818152602001828054801561088d57602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019060010190808311610843575b50505050509050955095509550505050909192565b6000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16638dc858bc836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b15801561096157600080fd5b505af1158015610975573d6000803e3d6000fd5b505050506040513d602081101561098b57600080fd5b81019080805190602001909291905050509050919050565b600080600090505b600180549050811015610af5576001818154811015156109c757fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff161415610ae8577f1d7ec9cf5dbc6968603a703fbc90ae0479ff995894d398f135e1599e287ac31f60006040518080602001828103825283818154600181600116156101000203166002900481526020019150805460018160011615610100020316600290048015610ad15780601f10610aa657610100808354040283529160200191610ad1565b820191906000526020600020905b815481529060010190602001808311610ab457829003601f168201915b50509250505060405180910390a160019150610d1f565b80806001019150506109ab565b610afe326108a2565b15610c2c5760013290806001815401808255809150509060018203906000526020600020016000909192909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602","17905550507f53e237c11bd95eb5d5a493eabfb3f2a39969f2eff53c2cc35ad2d84e1d297db060006040518080602001828103825283818154600181600116156101000203166002900481526020019150805460018160011615610100020316600290048015610c155780601f10610bea57610100808354040283529160200191610c15565b820191906000526020600020905b815481529060010190602001808311610bf857829003601f168201915b50509250505060405180910390a160019150610d1f565b7f4bcb3a509ee493cf5fe3a9e19653e00f924cbd58de4704e144cdb2f6fa5edf1f60003260405180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818154600181600116156101000203166002900481526020019150805460018160011615610100020316600290048015610d0b5780601f10610ce057610100808354040283529160200191610d0b565b820191906000526020600020905b815481529060010190602001808311610cee57829003601f168201915b5050935050505060405180910390a1600091505b50905600a165627a7a7230582002e0cd63fdfe922cba39d94125e5f77696b57514f90a8a5f95ad1e4eb689e9fe0029"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[],\"name\":\"getEvidence\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"address[]\"},{\"name\":\"\",\"type\":\"address[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"addSignatures\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"getSigners\",\"outputs\":[{\"name\":\"\",\"type\":\"address[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"factoryAddr\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"CallVerify\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"evi\",\"type\":\"string\"},{\"name\":\"addr\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"evi\",\"type\":\"string\"}],\"name\":\"addSignaturesEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"evi\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"newSignaturesEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"evi\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"errorNewSignaturesEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"evi\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"errorAddSignaturesEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"evi\",\"type\":\"string\"}],\"name\":\"addRepeatSignaturesEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"evi\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"errorRepeatSignaturesEvent\",\"type\":\"event\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final String FUNC_GETEVIDENCE = "getEvidence";

    public static final String FUNC_ADDSIGNATURES = "addSignatures";

    public static final String FUNC_GETSIGNERS = "getSigners";

    public static final String FUNC_FACTORYADDR = "factoryAddr";

    public static final String FUNC_CALLVERIFY = "CallVerify";

    public static final Event ADDSIGNATURESEVENT_EVENT = new Event("addSignaturesEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event NEWSIGNATURESEVENT_EVENT = new Event("newSignaturesEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event ERRORNEWSIGNATURESEVENT_EVENT = new Event("errorNewSignaturesEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event ERRORADDSIGNATURESEVENT_EVENT = new Event("errorAddSignaturesEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event ADDREPEATSIGNATURESEVENT_EVENT = new Event("addRepeatSignaturesEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event ERRORREPEATSIGNATURESEVENT_EVENT = new Event("errorRepeatSignaturesEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    protected Evidence(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public Tuple3<String, List<String>, List<String>> getEvidence() throws ContractException {
        final Function function = new Function(FUNC_GETEVIDENCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<DynamicArray<Address>>() {}, new TypeReference<DynamicArray<Address>>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple3<String, List<String>, List<String>>(
                (String) results.get(0).getValue(), 
                convertToNative((List<Address>) results.get(1).getValue()), 
                convertToNative((List<Address>) results.get(2).getValue()));
    }

    public TransactionReceipt addSignatures() {
        final Function function = new Function(
                FUNC_ADDSIGNATURES, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void addSignatures(TransactionCallback callback) {
        final Function function = new Function(
                FUNC_ADDSIGNATURES, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForAddSignatures() {
        final Function function = new Function(
                FUNC_ADDSIGNATURES, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<Boolean> getAddSignaturesOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_ADDSIGNATURES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public List getSigners() throws ContractException {
        final Function function = new Function(FUNC_GETSIGNERS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        List<Type> result = (List<Type>) executeCallWithSingleValueReturn(function, List.class);
        return convertToNative(result);
    }

    public String factoryAddr() throws ContractException {
        final Function function = new Function(FUNC_FACTORYADDR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public Boolean CallVerify(String addr) throws ContractException {
        final Function function = new Function(FUNC_CALLVERIFY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public List<AddSignaturesEventEventResponse> getAddSignaturesEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDSIGNATURESEVENT_EVENT, transactionReceipt);
        ArrayList<AddSignaturesEventEventResponse> responses = new ArrayList<AddSignaturesEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddSignaturesEventEventResponse typedResponse = new AddSignaturesEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.evi = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeAddSignaturesEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(ADDSIGNATURESEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeAddSignaturesEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(ADDSIGNATURESEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<NewSignaturesEventEventResponse> getNewSignaturesEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NEWSIGNATURESEVENT_EVENT, transactionReceipt);
        ArrayList<NewSignaturesEventEventResponse> responses = new ArrayList<NewSignaturesEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewSignaturesEventEventResponse typedResponse = new NewSignaturesEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.evi = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.addr = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeNewSignaturesEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(NEWSIGNATURESEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeNewSignaturesEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(NEWSIGNATURESEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<ErrorNewSignaturesEventEventResponse> getErrorNewSignaturesEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ERRORNEWSIGNATURESEVENT_EVENT, transactionReceipt);
        ArrayList<ErrorNewSignaturesEventEventResponse> responses = new ArrayList<ErrorNewSignaturesEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ErrorNewSignaturesEventEventResponse typedResponse = new ErrorNewSignaturesEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.evi = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.addr = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeErrorNewSignaturesEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(ERRORNEWSIGNATURESEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeErrorNewSignaturesEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(ERRORNEWSIGNATURESEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<ErrorAddSignaturesEventEventResponse> getErrorAddSignaturesEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ERRORADDSIGNATURESEVENT_EVENT, transactionReceipt);
        ArrayList<ErrorAddSignaturesEventEventResponse> responses = new ArrayList<ErrorAddSignaturesEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ErrorAddSignaturesEventEventResponse typedResponse = new ErrorAddSignaturesEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.evi = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.addr = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeErrorAddSignaturesEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(ERRORADDSIGNATURESEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeErrorAddSignaturesEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(ERRORADDSIGNATURESEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<AddRepeatSignaturesEventEventResponse> getAddRepeatSignaturesEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDREPEATSIGNATURESEVENT_EVENT, transactionReceipt);
        ArrayList<AddRepeatSignaturesEventEventResponse> responses = new ArrayList<AddRepeatSignaturesEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddRepeatSignaturesEventEventResponse typedResponse = new AddRepeatSignaturesEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.evi = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeAddRepeatSignaturesEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(ADDREPEATSIGNATURESEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeAddRepeatSignaturesEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(ADDREPEATSIGNATURESEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<ErrorRepeatSignaturesEventEventResponse> getErrorRepeatSignaturesEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ERRORREPEATSIGNATURESEVENT_EVENT, transactionReceipt);
        ArrayList<ErrorRepeatSignaturesEventEventResponse> responses = new ArrayList<ErrorRepeatSignaturesEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ErrorRepeatSignaturesEventEventResponse typedResponse = new ErrorRepeatSignaturesEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.evi = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.addr = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeErrorRepeatSignaturesEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(ERRORREPEATSIGNATURESEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeErrorRepeatSignaturesEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(ERRORREPEATSIGNATURESEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static Evidence load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Evidence(contractAddress, client, credential);
    }

    public static Evidence deploy(Client client, CryptoKeyPair credential, String evi, String addr) throws ContractException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(evi), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(addr)));
        return deploy(Evidence.class, client, credential, getBinary(client.getCryptoSuite()), encodedConstructor);
    }

    public static class AddSignaturesEventEventResponse {
        public TransactionReceipt.Logs log;

        public String evi;
    }

    public static class NewSignaturesEventEventResponse {
        public TransactionReceipt.Logs log;

        public String evi;

        public String addr;
    }

    public static class ErrorNewSignaturesEventEventResponse {
        public TransactionReceipt.Logs log;

        public String evi;

        public String addr;
    }

    public static class ErrorAddSignaturesEventEventResponse {
        public TransactionReceipt.Logs log;

        public String evi;

        public String addr;
    }

    public static class AddRepeatSignaturesEventEventResponse {
        public TransactionReceipt.Logs log;

        public String evi;
    }

    public static class ErrorRepeatSignaturesEventEventResponse {
        public TransactionReceipt.Logs log;

        public String evi;

        public String addr;
    }
}
