#include <stdio.h>
#include <stdlib.h>
#include <locale.h>/*setlocale(LC_ALL,"Portuguese");*/
#include <string.h>
#include "estruturas.h"
#include "manipula_listas.h"
#include "ficheiros.h"

void RetiraEnter(char *str){
    int i=0;
    while(str[i]!= '\n')
        i++;
    str[i]='\0';

}

int lista_vazia_alunos(Lista_alunos *li)
{
    if(li == NULL || *li==NULL) return 1;
    return 0;
}
int lista_vazia_disciplinas(Lista_disciplinas *li)
{
    if(li == NULL || *li==NULL) return 1;
    return 0;
}

int lista_vazia_exames(Lista_exames *li)
{
    if(li == NULL || *li==NULL) return 1;
    return 0;
}



int insere_lista_ordenada(Lista_alunos* node){

    system("cls");/*nome*/
    struct dados_aluno aux;
    printf("Insira o nome do aluno:\n");
    fgets(aux.nome,60,stdin);
    RetiraEnter(aux.nome);
    fflush(stdin);

    system("cls");/*numero aluno*/
    printf("Insira o número do aluno:\n");
    scanf("%d",&aux.n_aluno);

    int flag=0;/* nome do curso*/
    while(flag==0){
        system("cls");
        printf("Insira o Curso:\n\t1-LEI\n\t2-LDM\n");
        scanf("%d",&flag);
            if(flag==1)
                aux.curso=1;

            else if(flag==2)

                aux.curso=2;
            else
                flag=0;
            system("cls");
        }


    system("cls");
    printf("Insira o ano de matricula:\n");
    scanf("%d",&aux.ano_matricula);

    int flagr=0;/* regime*/
    while(flagr==0){
            system("cls");
            printf("Insira o seu regime:\n\n\t1-Normal\n\t2-Trabalhador\n\t3-Atleta\n\t4-Dirigente Associativo\n\t5-Erasmus\n\n");
            scanf("%d",&flagr);

            if(flagr==1 || flagr==2 || flagr==3 || flagr==4 || flagr==5)
                aux.regime=flagr;
            else
                flagr=0;
    }
    if(verifica_aluno(node, aux.n_aluno)){
        printf("o aluno já existe\n");
        return 0;
    }
    if(node == NULL) return 0;
    t_aluno *no = (t_aluno*) malloc(sizeof(t_aluno));
    if(no == NULL) return 0;
    no->DADOS_ALUNO = aux;

    if(lista_vazia_alunos(node)){//insre no inicio//

        no->prox = (*node);
        *node= no;
        return 1;
    }
    else{
        t_aluno *ant, *atual = *node;
        while(atual != NULL && atual->DADOS_ALUNO.n_aluno < aux.n_aluno){
            ant = atual;
            atual = atual->prox;
        }
        if(atual == *node){//insre inicio//
            no->prox = (*node);
            *node = no;
        }
        else{
            no->prox = ant->prox;
            ant->prox = no;
        }
        return 1;
    }
}

int remove_aluno(Lista_alunos *node, int num)
{
    if(node == NULL) return 0;
    t_aluno *ant, *no = *node;
    while(no != NULL && no->DADOS_ALUNO.n_aluno != num){
        ant = no;
        no = no->prox;
    }
    if(no == NULL) return 0;

    if(no == *node)//remover o primeiro?//
        *node = no->prox;
    else
        ant->prox = no->prox;
    free(no);
    return 1;

}

int insere_disciplina(Lista_disciplinas *node)
{
    t_disciplina *no = (t_disciplina*) malloc(sizeof(t_disciplina));
    system("cls");
    struct dados_disciplina aux;
    printf("Insira o nome da disciplna:\n");
    fgets(no->DADOS_DISCIPLINA.nome,20,stdin);
    RetiraEnter(no->DADOS_DISCIPLINA.nome);


    system("cls");
    printf("Insira o nome do docente:\n");
    fgets(no->DADOS_DISCIPLINA.nome_docente,60,stdin);
    RetiraEnter(no->DADOS_DISCIPLINA.nome_docente);

    if(!verifica_disciplina(node,no->DADOS_DISCIPLINA.nome)){
        no->prox = (*node);
        *node = no;
        return 1;
    }
    else{
        printf("A disciplina já existe\n");
        return 0;
    }

}



int remove_disciplina(Lista_disciplinas *node, char *nome)
{
    if(node == NULL) return 0;
    t_disciplina *ant, *no = *node;
    while(no != NULL && strcmp(no->DADOS_DISCIPLINA.nome, nome)){
        ant = no;
        no = no->prox;
    }
    if(no == NULL) return 0;

    if(no == *node)//remover o primeiro?//
        *node = no->prox;
    else
        ant->prox = no->prox;
    free(no);
    return 1;
}



int libera_alunos(Lista_alunos *node)
{
    if(node!=NULL){
        t_aluno *no;
        while((*node)!=NULL){
            no=*node;
            *node=(*node)->prox;
            free(no);
        }
        free(node);
    }

}

int libera_disciplinas(Lista_disciplinas *node)
{
    if(node!=NULL){
        t_disciplina *no;
        while((*node)!=NULL){
            no=*node;
            *node=(*node)->prox;
            free(no);
        }
        free(node);
    }

}

int libera_exames(Lista_exames *node)
{
       if(node!=NULL){
        t_exames *no;
        while((*node)!=NULL){
            no=*node;
            *node=(*node)->prox;
            free(no);
        }
        free(node);
    }


}



int verifica_aluno(Lista_alunos *node, int num)
{

	if(node == NULL) return 0;
	t_aluno *no = *node;
	while(no != NULL && no->DADOS_ALUNO.n_aluno != num){
        printf("entrou no while");
		no = no->prox;
	}
	system("pause");
	if(no == NULL) return 0;

	return 1;
}

int verifica_disciplina(Lista_disciplinas *node,char *str)
{

	if(node == NULL) return 0;
	t_disciplina *no = *node;

	while(no != NULL && !strcmp(no->DADOS_DISCIPLINA.nome, str)){
		no = no->prox;
	}
	if(no == NULL) return 0;

	return 1;

}

void ler_data(Data *d){
    printf("Insira o dia: ");
    scanf("%d", &(d->dia));
    printf("Insira o mes: ");
    scanf("%d", &(d->mes));
    printf("Insira o ano: ");
    scanf("%d", &(d->ano));
}

void imprimir_data(Data *d){
    printf("%d/%d/%d\n", d->dia, d->mes, d->ano);
}

int comparacao_data(Data a, Data b){
    if((a.dia==b.dia) && (a.mes==b.mes) && (a.ano==b.mes)){
        return 1;
    }
    if((a.ano<b.ano) || (a.mes<b.mes) || (a.dia<b.dia)){
        return 2;
    }
    else{
        return 0;
    }
}

void ler_hora(Hora *h)
{
    printf("Insira a hora inical: xx:xx\n");
    scanf("%d", &(h->h_inicio));
    scanf("%d", &(h->m_inicio));
    printf("\nInsira a hora final: xx:xx\n");
    scanf("%d", &(h->h_final));
    scanf("%d", &(h->m_final));

}

void imprimir_hora(Hora *h){
    printf("%d:%d -> %d:%d\n",h->h_inicio, h->h_inicio, h->h_final, h->m_final);
}

int comparacao_hora(Hora hor, int h, int m)
{
	if(hor.h_final<h)
		return 1;
	else if(hor.h_final==h && hor.m_final<=m)
		return 1;
	else
		return 0;

}

int criar_exame(Lista_exames *node, Lista_disciplinas *node_disciplinas)
{
    t_exames *no = (t_exames*) malloc(sizeof(t_exames));

    system("cls");/*Nome da disciplina*/
    printf("Insira o nome da disciplina:.\n");
    fflush(stdin);
    fgets(no->DADOS_EXAME.disciplina,10,stdin);


    verifica_disciplina(node_disciplinas,no->DADOS_EXAME.disciplina );
    system("cls");/*acrescentar proteçoes*/
    printf("Insira a epoca do exame:\n\n1- Normal\n2 - Recurso\n3 - Especial\n");
    fflush(stdin);
    scanf("%d",&(no->DADOS_EXAME.epoca));

    system("cls");
    ler_data(&(no->data_exame));

    system("cls");
    ler_hora(&(no->hora_exame));


    no->ALUNOS_INSCRITOS = cria_lista_alunos();
    no->DADOS_EXAME.numero_alunos = 0;

    /*antes de criar falta verificar datas e horas*/

    no->prox = (*node);
    *node=no;

    return 1;
}

void imprimir_epoca(int n){

    if(n==1)
        printf(" Normal");
    else if(n==2)
        printf(" Recurso");
    else if(n==3)
        printf(" Especial");
}

int listar_exames(Lista_exames *node)
{
    t_exames *copia = *node;
    system("CLS");
    printf("Abaixo estão listados os exames, com respetiva data e época\n");

    while(copia != NULL)
    {
        printf("%s ",copia->DADOS_EXAME.disciplina);
        imprimir_data(&(copia->data_exame));
        imprimir_epoca(copia->DADOS_EXAME.epoca);
        copia = copia->prox;
        printf("\n");
    }
    return 1;
}

int apagar_exames(Lista_exames *node)
{
    struct data d;
    ler_data(&d);
    int h,m;
    printf("Insira a hora:\n");
    scanf("%d\n",&h);
    printf("Insira a minuto:\n");
    scanf("%d\n",&m);

    t_exames *ant, *no = *node;
    while(no != NULL){

        if(comparacao_data(no->data_exame,d) == 2)
            {
            if(no == *node)/*remover primeiro*/
                *node=no->prox;
            else
                ant->prox = no->prox;
            free(no);
        }

        else if(comparacao_data(no->data_exame,d)&&comparacao_hora(no->hora_exame,h,m))
            {
            if(no == *node)/*remover primeiro*/
                *node=no->prox;
            else
                ant->prox = no->prox;
            free(no);
        }

        ant = no;
        no = no->prox;
    }
    return 1;

}

int verificar_exame(Lista_exames *node, char *disci, int epo)
{
    t_exames *copia = *node;
    while(copia != NULL && (strcmp(copia->DADOS_EXAME.disciplina,disci) && copia->DADOS_EXAME.epoca!=epo)){
        copia = copia->prox;
    }
    if(copia==NULL)
        return 0;

    return 1;

}

int verifica_aluno_em_exame(Lista_exames *node, char *disci, int epo, int num)
{

    t_exames *copia= *node;
        while(copia != NULL && (strcmp(copia->DADOS_EXAME.disciplina,disci) && copia->DADOS_EXAME.epoca!=epo)){
        copia = copia->prox;
    }
    if(copia==NULL){
        printf("O exame nao foi criado.\n");
        return 0;
    }
    else{
        t_aluno *no = *copia->ALUNOS_INSCRITOS;
        while(no != NULL && no->DADOS_ALUNO.n_aluno != num){
		no = no->prox;
        }
        if(no==NULL){
            return 0;
    }

    return 1;

    }
}

int listar_alunos_inscritos(Lista_exames *node){

    char str[10];
    printf("Insira o nome da disciplina:\n");
    fflush(stdin);
    fgets(str, 10, stdin);
    RetiraEnter(str);

    system("cls");
    int n;
    while(n<0 && n>3){
        printf("Insira a epoca do exame:\n\n1 - Normal\n2 - Recurso\n3 - Especial\n");
        scanf("%d",&n);
    }

        t_exames *copia = *node;
    while(copia != NULL && (strcmp(copia->DADOS_EXAME.disciplina,str) && copia->DADOS_EXAME.epoca!=n)){
        copia = copia->prox;
    }
    system("cls");
    if(copia==NULL){
        printf("Exame não exite!!");
        return 0;
    }

    else{
        printf("Os alunos inscritos %s na epoca",str);
        imprimir_epoca(n);
        printf(":\n");
        /*percorrer todos os alunos e dar print*/
        t_aluno *aux = *(copia->ALUNOS_INSCRITOS);
        while( aux!=NULL){
                printf("\n%s",aux->DADOS_ALUNO.nome);

        }
        return 1;
    }


}



int inscrever_aluno_exame(Lista_exames *node, Lista_alunos *node_al){

    system("cls");
    char disci[10];
    printf("Insira o nome da disciplina do exame onde quer inscrever o aluno:\n");
    fflush(stdin);
    fgets(disci, 10, stdin);
    RetiraEnter(disci);

    system("cls");
    int n;
    while(n<0 && n>3){
        printf("Insira a epoca do exame:\n\n1 - Normal\n2 - Recurso\n3 - Especial\n");
        scanf("%d",&n);
    }

    t_exames *copia = *node;
    while(copia != NULL && (strcmp(copia->DADOS_EXAME.disciplina,disci) && copia->DADOS_EXAME.epoca!=n)){
        copia = copia->prox;
    }
    system("cls");
    if(copia==NULL){
        printf("Exame não exite!!");
        return 0;
    }

    int n_al;
    printf("Insira o numero do aluno que pretende inscrever:\n");
    fflush(stdin);
    scanf("%d",&n_al);

    if(verifica_aluno(node_al, n_al)){

            if(node_al == NULL){
                printf("Não há alunos inscritos.\n");
                return 0;
            }
            t_aluno *inscrito = (t_aluno*) malloc(sizeof(t_aluno));
            t_aluno *no = *node_al;
            while( no!= NULL && no->DADOS_ALUNO.n_aluno != n_al){
                no = no->prox;
            }

            if(no == NULL) return 0;

            else if(n!= 3 ||  no->DADOS_ALUNO.regime>1){
                printf("O aluno não pode ser inscrito neste exame.");
                return 0;
            }
            else{
                inscrito->DADOS_ALUNO = no->DADOS_ALUNO;
                inscrito->prox=(*copia->ALUNOS_INSCRITOS);
                *(copia->ALUNOS_INSCRITOS)=inscrito;
                return 1;

            }


            copia->DADOS_EXAME.numero_alunos = copia->DADOS_EXAME.numero_alunos+1;
            /*if(copia->DADOS_EXAME.numero_alunos%30==1){
                printf("Necessita reservar uma sala:\n);
                fflush(stdin);
                scanf("%s",copia->salas[copia->DADOS_EXAME.numero_alunos/30][]);
            }*/

        return 1;
    }

}


int remove_aluno_exame(Lista_exames *node){

    system("cls");
    printf("Insira o nome da disciplina do exame para o qual pretende remover o aluno:\n");
    char disci[10];
    fflush(stdin);
    fgets(disci,10,stdin);

    system("cls");
    int n;
    while(n<0 && n>3){
        printf("Insira a epoca do exame:\n\n1 - Normal\n2 - Recurso\n3 - Especial\n");
        scanf("%d",&n);
    }

    t_exames *copia = *node;
    while(copia != NULL && (strcmp(copia->DADOS_EXAME.disciplina,disci) && copia->DADOS_EXAME.epoca!=n)){
        copia = copia->prox;
    }
    system("cls");
    if(copia==NULL){
        printf("Exame não exite!!");
        return 0;
    }


    system("cls");
    int numero;
    printf("Insira o numero do aluno que pretende remover:\n");
    fflush(stdin);
    scanf("%d",&numero);

    if(!remove_aluno(copia->ALUNOS_INSCRITOS, numero)){
        printf("\nO aluno não estava inscrito");
        return 0;
       }
    else{
        printf("\naluno removido.");
        return 1;
    }


}

