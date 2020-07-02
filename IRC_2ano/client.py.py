# chat_client.py

import sys
import socket
import select
import os
def chat_client():
    if(len(sys.argv) < 3) :
        print 'Usage : python chat_client.py hostname port'
        sys.exit()

    host = sys.argv[1]
    port = int(sys.argv[2])
     
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.settimeout(2)

    try :
        s.connect((host, port))
    except :
        print 'Unable to connect'
        sys.exit()
    os.system('clear')
    user = raw_input("Escreva o seu username: ") 
    s.send(user)
    print 'Connected to remote host. Pode comecar a mandar mensagens\n /help/ -> Para ajuda'
    sys.stdout.flush()
     
    while 1:
        socket_list = [sys.stdin, s]
         
        
        ready_to_read,ready_to_write,in_error = select.select(socket_list , [], [])
         
        for sock in ready_to_read:             
            if sock == s:
               
                data = sock.recv(4096)
                if not data :
                    print '\nDisconnected from chat server'
                    sys.exit()
                else :
                    
                    sys.stdout.write(data)
                    sys.stdout.flush()     
            
            else :
             
                msg = sys.stdin.readline()
                s.send(msg)
                sys.stdout.flush() 

if __name__ == "__main__":

    sys.exit(chat_client())
