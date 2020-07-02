# chat_server.py
 
import sys
import socket
import select
import os.path
import os
HOST = '' 
SOCKET_Dict = {} # {sock: [username, codigo de status]}
groups = {}	#{nomegrupo: [users do grupo]}
SOCKET_LIST = [] # [sockets ativas]
Block = {}	#{user : [usersbloqueados]}
RECV_BUFFER = 4096 
PORT = 9009
def chat_server():

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind((HOST, PORT))
    server_socket.listen(10)
 
    """nome = server_socket.recv(RECV_BUFFER)
    SOCKET_Dict.setDefault(server_socket, nome)
    """
    SOCKET_LIST.append(server_socket)
    os.system('clear')
    print "Chat server started on port " + str(PORT)
 
    while 1:

       
        ready_to_read,ready_to_write,in_error = select.select(SOCKET_LIST,[],[],0)
      
        for sock in ready_to_read:
            
            if sock == server_socket: 
                sockfd, addr = server_socket.accept()
                nome = sockfd.recv(RECV_BUFFER)
                Block.setdefault(sockfd, [])
                SOCKET_LIST.append(sockfd)
                print "Client (%s) connected" % nome
		for a in SOCKET_Dict.keys():
			sockfd.send("[" + SOCKET_Dict[a][0] + "] is online\n")
                SOCKET_Dict.setdefault(sockfd, [nome, '']) 
                broadcast(server_socket, sockfd, "[%s] is online\n" % nome)
             
            
            else:
                 
                try:
                    
                    data = sock.recv(RECV_BUFFER)	    
                    if("/help/" in data):
                        help(sock, 0) 
		    elif("/block/" in data):
			data = data.split("/")
			if (len(data) != 4):
				help(sock, 0)
			for a in SOCKET_Dict.keys():
				if (data[2] == SOCKET_Dict[a][0]):
					block(sock, data[2], 0)
		    elif("/ban/" in data):
			data = data.split("/")
			if (len(data) != 5):
				help(sock, 0)
			else:
				ban(sock, data[2], data[3])
		    elif("/unblock/" in data):
			data = data.split("/")
			if (len(data) != 4):
				help(sock, 0)
			for a in SOCKET_Dict.keys():
				if (data[2] == SOCKET_Dict[a][0]):
					block(sock, data[2], 1)	
		    elif("/exit/" in data):
           			SOCKET_Dict[sock][1] = ("")
				sock.send("Saiu da conversacao anterior.\n")
                    elif ("/pm/" in data):
			data = data.split("/")
			if(len(data) != 4):
				help(sock, 0)#print("Erro, mensagem privada: /pm/<user>/<message>")
			for a in SOCKET_Dict.keys():
				if (data[2] == SOCKET_Dict[a][0]):
					SOCKET_Dict[sock][1] = ("/" + data[1] + "/" + data[2] + "/")
					read(server_socket, sock, data[2])

		    elif ("/create/" in data):
			data = data.split("/")
			if(len(data) != 4):
				help(sock, 0)#print("Erro, mensagem privada: /pm/<user>/<message>")
			elif(data[2]  in groups.keys()):
				help(sock, 2)#print("grupo ja existe)
			else:
				create(sock, data[2])				
                    elif ("/invite/" in data):
			data = data.split("/")
			if(len(data) != 5):
				help(sock, 0)#print("Erro, mensagem privada: /pm/<user>/<message>"
			else:					
				invite(sock, data[2], data[3])
		    elif ("/group/" in data):
			data = data.split("/")
			if (SOCKET_Dict[sock][0] in (groups[data[2]])):
				SOCKET_Dict[sock][1] = ("/" + data[1] + "/" + data[2] + "/")
				readg(server_socket, sock, data[2])
			else:
				sock.send("Nao tem permissao para aceder a esse grupo.\n")
		    elif("/pm/" in SOCKET_Dict[sock][1]):
			private(server_socket, sock, data)
		    elif("/group/" in SOCKET_Dict[sock][1]):
			codigo = SOCKET_Dict[sock][1].split("/")
			if(SOCKET_Dict[sock][0] in groups[codigo[2]]):
				codigo = SOCKET_Dict[sock][1].split("/")
				messageg(sock,codigo[2], data)
           
		    else:
			help(sock,0)
			
                    
 

                # exception 
                except:
                    help(sock,0)
                    continue

    server_socket.close()
def readg(server_socket, sock, nomeg):
    s = nomeg +".txt"
    if(os.path.isfile(s)):
    	fich=open(s, 'r')
    else:	
	sock.send("O grupo nao esta criado.\n")
	return
    fich.seek(0)
    sock.send(fich.read())
    fich.close()
def block(sock, nome, flag): # flag = 0 -> Bloqueia || flag = 1 -> Desbloqueia || flag = 2 -> Return False se Blockeado
	if (flag == 0):
		Block[sock].append(nome)
		sock.send("O user " + nome + " foi bloqueado.\n")
	elif (flag == 1):
		Block[sock].remove(nome)
		sock.send("O user " + nome + " foi desbloqueado.\n")
	elif (flag == 2):
		if(nome in Block[sock]):
			sock.send("Tens esse user bloqueado, try /unblock/" + nome + "/.\n")
			return False
		nome1 = SOCKET_Dict[sock][0]
		for a in SOCKET_Dict.keys():
			if (nome in SOCKET_Dict[a]):
				sock1 = a
		if (nome1 in Block[sock1]):
			sock.send("Foste bloquado por esse user. \n")
			return False
	return True
def ban(sock, nomeg, nome):
	if (SOCKET_Dict[sock][0] == groups[nomeg][0]):
		if (nome in groups[nomeg]):
			sock.send("O user " + nome + " foi removido!\n")
			groups[nomeg].remove(nome)
	else:
		sock.send("Nao podes remover users.\n")

def messageg(sock, nomeg, message):
	fich=open(nomeg+".txt",'a')
	fich.write("[" + SOCKET_Dict[sock][0] + "] " + message)
	for no in groups[nomeg]:
		for aux in SOCKET_Dict.keys():
			if(SOCKET_Dict[aux][0] == no):
				if(("/group/" + nomeg + "/") == SOCKET_Dict[aux][1] and aux != sock):
					try:
						aux.send("[" + SOCKET_Dict[sock][0] + "] " + message)
					except:
						aux.close()
		        			# broken socket, remove it
		        			if aux in SOCKET_Dict:
		           				SOCKET_Dict.pop(aux)
		            				SOCKET_LIST.remove(aux)
				elif(aux != sock):
					try:
						aux.send("Tem notificacoes do grupo [" + nomeg + "].\n")
					except:
						aux.close()
		        			# broken socket, remove it
		        			if aux in SOCKET_Dict:
		           				SOCKET_Dict.pop(aux)
		            				SOCKET_LIST.remove(aux)


def create(sock, nomeg):
	fich = open(nomeg + ".txt", 'w')
	groups.setdefault(nomeg,[SOCKET_Dict[sock][0]])
	fich.write("\t\tCONVERSA " + nomeg + "\n")
	sock.send("O grupo " + nomeg + " foi criado com sucesso.\n")
	print("O grupo "+nomeg + " foi criado.\n")
	fich.close()

def invite(sock, nomeg, conv):
	if(nomeg not in groups.keys()):
		help(sock,1)
	elif(conv in groups[nomeg]):
		return
	elif(SOCKET_Dict[sock][0] not in groups[nomeg]):
		help(sock,1)
	for a in SOCKET_Dict.keys():
		if (SOCKET_Dict[a][0] == conv):
			sock.send("O user " + conv + " foi adicionado com sucesso.\n")
			groups[nomeg].append(conv)
			a.send("Foste adicionado ao grupo " + nomeg + ".\n")

def broadcast (server_socket, sock, message):
    for socket in SOCKET_Dict.keys():
        if socket != server_socket and socket != sock :
            try :
                socket.send(message)
            except :
                socket.close()
                if socket in SOCKET_Dict:
                    SOCKET_Dict.pop(socket)
                    SOCKET_LIST.remove(socket)
def help(sock ,erro):
    if (erro == 0):
	sock.send("-"*10 + "HELP" + "-"*10 + "\n Comecar conversa privada: /pm/<user>/\nCriar grupo: /create/<nomegrupo>/\nAdicionar ao grupo: /invite/<nomegrupo>/<user>/\nIniciar conversa de Grupo: /group/<nomegrupo>/\nBloquear\Desbloquear user: /block\unblock/<user>/\nBanir user de grupo: /ban/<nomegrupo>/<user>/\nSair da conversacao anterior: /exit/\n\nNOTA: Confirme se colocou os <user>'s corretamente!\n")
    elif(erro ==1):
	sock.send("-"*10 + "ERRO" + "-"*10 + "\n" + "Esse user nao existe" + ".\n")
    elif(erro ==2):
	sock.send("-"*10 + "ERRO" + "-"*10 + "\n" + "Esse nome de grupo ja foi utilizado" + ".\n")
def read(server_socket, sock, nome):
    
    s1 = SOCKET_Dict[sock][0] + nome+ "MsgPrivada.txt"
    s2 = nome + SOCKET_Dict[sock][0] + "MsgPrivada.txt"
    if(os.path.isfile(s1)):
    	fich=open(s1, 'r')
    elif(os.path.isfile(s2)):
	fich=open(s2, 'r')
    else:
	sock.send("Nao tem mensagens desse user.\n")
	return
    fich.seek(0)
    sock.send(fich.read())
    fich.close()


def private (server_socket, sock, message):
    codigo = SOCKET_Dict[sock][1].split("/")
    nomerecetor = codigo[2]
    if (block(sock,nomerecetor,2)):	    
	    s1 = SOCKET_Dict[sock][0] + nomerecetor + "MsgPrivada.txt"
	    s2 = nomerecetor + SOCKET_Dict[sock][0] + "MsgPrivada.txt"
	    if(os.path.isfile(s1)):
	    	fich=open(s1, 'a')
	    elif(os.path.isfile(s2)):
		fich=open(s2, 'a')
	    else:
		fich=open(s1,'a')
	    for socket in SOCKET_Dict.keys():
		if(nomerecetor == SOCKET_Dict[socket][0]):
		    if socket != server_socket and socket != sock:
			if(("/pm/" + SOCKET_Dict[sock][0] + "/") == SOCKET_Dict[socket][1] ):
				try:
					socket.send("[" + SOCKET_Dict[sock][0] + "] " + message)
					fich.write("[" + SOCKET_Dict[sock][0] + "] " + message)
				except:
					socket.close()
		            		if socket in SOCKET_Dict:
		                		SOCKET_Dict.pop(socket)
			else:
		       		try :				
					socket.send("Tem uma pm do user " + SOCKET_Dict[sock][0] + "\n")
		            		fich.write("[" + SOCKET_Dict[sock][0] + "] " + message)
		        	except :                   
		            		socket.close()
		            		if socket in SOCKET_Dict:
		                		SOCKET_Dict.pop(socket)
	    fich.close()
 
if __name__ == "__main__":
	
    sys.exit(chat_server())
    
