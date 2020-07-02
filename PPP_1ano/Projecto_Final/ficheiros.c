#include <stdio.h>
#include <stdlib.h>
#include <locale.h>/*setlocale(LC_ALL,"Portuguese");*/
#include <string.h>
#include "estruturas.h"
#include "manipula_listas.h"
#include "ficheiros.h"



int carrega_lista_alunos(Lista_alunos *node)
{
    FILE *fp;
    fp = fopen("C:\\Users\\User\\Desktop\\PPP\\Nova pasta\\Projectos\\Projecto_Final\\alunos", "r");

    t_aluno *no = (t_aluno*) malloc(sizeof(t_aluno));

    while((fscanf(fp,"%s",no->DADOS_ALUNO.nome))!=EOF){

        fscanf(fp,"%d %d %d %d",&(no->DADOS_ALUNO.n_aluno),&(no->DADOS_ALUNO.ano_matricula),&(no->DADOS_ALUNO.regime),&(no->DADOS_ALUNO.curso));



        if((*node)== NULL ){
            no->prox = (*node);
            *node = no;
        }

        else{
            t_aluno *aux = *node;
            while(aux->prox != NULL){
                aux = aux->prox;
            }
            aux->prox = no;

        }

        t_aluno *no = (t_aluno*) malloc(sizeof(t_aluno));

    }
    return 1;

    fclose(fp);
}

int carrega_ficheiro_alunos(Lista_alunos *node)
{
    FILE *fp;
    fp = fopen("C:\\Users\\User\\Desktop\\PPP\\Nova pasta\\Projectos\\Projecto_Final\\alunos", "w");


    if(node == NULL) return 0;
    t_aluno *copia= *node;

    while(copia != NULL){


        fprintf(fp,"%s ",copia->DADOS_ALUNO.nome);
        fprintf(fp,"%d",copia->DADOS_ALUNO.n_aluno);
        fprintf(fp," %d",copia->DADOS_ALUNO.ano_matricula);
        fprintf(fp," %d",copia->DADOS_ALUNO.regime);
        fprintf(fp," %d\n",copia->DADOS_ALUNO.curso);
        copia=copia->prox;
    }
    fclose(fp);
    return 1;
}


int carrega_lista_disciplinas(Lista_disciplinas *node)
{
    FILE *fp;
    fp = fopen("C:\\Users\\User\\Desktop\\PPP\\Nova pasta\\Projectos\\Projecto_Final\\disciplinas", "r");

    t_disciplina *no = (t_disciplina*) malloc(sizeof(t_disciplina));

    while((fscanf(fp,"%s %s",no->DADOS_DISCIPLINA.nome,no->DADOS_DISCIPLINA.nome_docente))!=EOF){

            no->prox = (*node);
            *node = no;


        t_disciplina *no = (t_disciplina*) malloc(sizeof(t_disciplina));

    }


    fclose(fp);
    return 1;
}


int carrega_ficheiro_disciplina(Lista_disciplinas *node)
{

    FILE *fp;
    fp = fopen("C:\\Users\\User\\Desktop\\PPP\\Nova pasta\\Projectos\\Projecto_Final\\disciplinas", "w");


    if(node == NULL) return 0;

    t_disciplina *copia= *node;

    while(copia != NULL){

            fprintf(fp,"%s ",copia->DADOS_DISCIPLINA.nome);
            fprintf(fp,"%s\n",copia->DADOS_DISCIPLINA.nome_docente);
            system("pause");
            copia = copia->prox;

    }
    fclose(fp);
    return 1;
}


/*Exames */

