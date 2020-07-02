#include <stdio.h>
#include <stdlib.h>lc
#include <locale.h>    /*setlocale(LC_ALL,"Portuguese");*/
#include <string.h>
#include "estruturas.h"
#include "manipula_listas.h"
#include "ficheiros.h"

Lista_exames * cria_lista_exames()
{
    Lista_exames *node_exame =(Lista_exames*)malloc(sizeof(Lista_exames));
    if(node_exame!=NULL)
        *node_exame=NULL;
    return node_exame;
}


Lista_alunos * cria_lista_alunos()/* so funciona se estiver no main*/
{
    Lista_alunos *node =(Lista_alunos*)malloc(sizeof(Lista_alunos));
    if(node!=NULL)
        *node=NULL;
    return node;
}

Lista_disciplinas * cria_lista_disciplinas()
{
    Lista_disciplinas *node_disciplina =(Lista_disciplinas*)malloc(sizeof(Lista_disciplinas));
    if(node_disciplina!=NULL)
        *node_disciplina=NULL;
    return node_disciplina;
}



