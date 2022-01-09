#!/bin/bash

function usage()
{
    echo " Usage : "
    echo "   bash evi_run.sh deploy"
#    echo "   bash evi_run.sh query    asset_account "
#    echo "   bash evi_run.sh register asset_account asset_amount "
#    echo "   bash evi_run.sh transfer from_asset_account to_asset_account amount "
    echo " "
    echo " "
    echo "examples : "
    echo "   bash evi_run.sh deploy "
#    echo "   bash evi_run.sh register  Asset0  10000000 "
#    echo "   bash evi_run.sh register  Asset1  10000000 "
#    echo "   bash evi_run.sh transfer  Asset0  Asset1 11111 "
#    echo "   bash evi_run.sh query Asset0"
#    echo "   bash evi_run.sh query Asset1"
    exit 0
}

    # $#：参数个数
    # lt：小于
    # &&：如果前一个命令执行成功则执行
#    case $1 in
#    deploy)
#            [ $# -lt 2 ] && { usage; }
#            ;;
#    case $1 in
#    register)
#            [ $# -lt 3 ] && { usage; }
#            ;;
#    transfer)
#            [ $# -lt 4 ] && { usage; }
#            ;;
#    query)
#            [ $# -lt 2 ] && { usage; }
#            ;;
#    *)
#        usage
#            ;;
#    esac

    # $@ 表示所有参数列表
    java -Djdk.tls.namedGroups="secp256k1" -cp 'apps/*:conf/:lib/*' org.fisco.bcos.evidence.client.EvidenceClient $@

