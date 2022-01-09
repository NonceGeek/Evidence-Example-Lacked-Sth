#!/usr/bin/python3
# encoding: utf-8

import os
import sys

JAVA_CMD='''java -Djdk.tls.namedGroups="secp256k1" ''' + \
'''-cp 'apps/*:conf/:lib/*' org.fisco.bcos.evidence.client.EvidenceClient'''

def do():
    if len(sys.argv)==1:
        help()
        return
    cmd = JAVA_CMD
    args = sys.argv[1:]
    for i in args:
        cmd += " " + i

    os.system(cmd)
    return

def help():
    helpMsg = '''
Usage
    '''
    print(helpMsg)
    return

if __name__ == '__main__':
    do()
    pass