#include <stdio.h>
#include <stdlib.h>
#include <locale.h>    /*setlocale(LC_ALL,"Portuguese");*/
#include <string.h>
#include "estruturas.h"
#include "manipula_listas.h"
#include "ficheiros.h"





void menu()
{
    printf("\tEscolha uma das seguintes opções:\n\n");
    printf("\t1 - Alunos\n");
    printf("\t2 - Exames\n");
    printf("\t3 - Disciplinas\n");
    printf("\t0 - Sair\n");
}

void ler_string(char string[],int size){
    int i;
    char c;

    for(i=0;i<size;i++){
        scanf("%c",&c);
        if(c=='\n'){
            string[i]='\0';
            break;
            }

        string[i]=c;
    }
    string[i]='\0';

}

void escolher_opt(char *opt){

        Lista_alunos *node_alunos;
        node_alunos = cria_lista_alunos();

        Lista_disciplinas *node_disciplinas;
        node_disciplinas = cria_lista_disciplinas();

        Lista_exames *node_exames;
        node_exames = cria_lista_exames();

        carrega_lista_alunos(node_alunos);
        carrega_lista_disciplinas(node_disciplinas);

    /*inserir os ficheiros nas listas*/
    while(1){
        menu();
        int size=2;
        char opt2='0';
        char str[size];
        ler_string(str,size);
        fflush(stdin);

        if(str[1]!='\0')
            *opt='a';

        else
            *opt=str[0];

            switch(*opt){
               case '1':

                        while(opt2=='0'){
                            system("CLS");
                            printf("\tPretende:\n\n");
                            printf("\t1 - Criar Aluno\n");
                            printf("\t2 - Alterar dados de Aluno\n");
                            printf("\t3 - Apagar Aluno\n");
                            ler_string(str,size);
                            fflush(stdin);
                            if(str[1]!='\0')
                                opt2='a';

                            else
                                opt2=str[0];



                                if(opt2=='1'){
                                    insere_lista_ordenada(node_alunos);
                                    system("pause");
                                }
                                else if(opt2=='2'){
                                    int n;
                                    system("cls");
                                    printf("Insira o número do aluno a alterar\n");
                                    scanf("%d",&n);
                                    if(verifica_aluno(node_alunos, n)){/*CONFIRMAR SE O NUMERO EXISTE*/
                                        remove_aluno(node_alunos, n);
                                        insere_lista_ordenada(node_alunos);
                                        printf("\nOs dados do aluno foram alterados com sucesso");
                                    }
                                    else{
                                        system("cls");
                                        printf("O numero do aluno que inseriu não existe");
                                    }
                                    system("pause");

                                }
                                else if(opt2=='3'){
                                    int n;
                                    system("cls");
                                    printf("Insira o numero do aluno que pretende remover:\n");
                                    scanf("%d",&n);
                                    if(verifica_aluno(node_alunos, n)){/*verificar se o numero existe*/
                                        remove_aluno(node_alunos, n);
                                        printf("\nO aluno foi removido com sucesso\n");
                                    }
                                    else{
                                        system("cls");
                                        printf("O numero do aluno que inseriu não existe\n");
                                        }
                                        system("pause");
                                }
                                else{
                                    opt2='0';
                                    system("CLS");
                                    printf("entrou no else");
                                    fflush(stdin);
                                    system("pause");
                                }

                        }

                    break;
                case '2':
                    while(opt2=='0'){
                            system("CLS");
                            printf("\tPretende:\n\n");
                            printf("\t1 - Criar Exame\n");
                            printf("\t2 - Listar Exames\n");
                            printf("\t3 - Inscrever alunos em Exames\n");
                            printf("\t4 - Remover alunos de Exames\n");
                            printf("\t5 - Listar alunos inscritos\n");
                            printf("\t6 - Apagar exames já realizados\n");
                            ler_string(str,size);
                            fflush(stdin);
                            if(str[1]!='\0')
                                opt2='a';

                            else
                                opt2=str[0];


                                if(opt2=='1'){
                                    system("cls");
                                    if(criar_exame(node_exames, node_disciplinas))
                                        printf("Foi criado o exame com sucesso\n");
                                    system("pause");
                                }
                                else if(opt2=='2'){
                                    listar_exames(node_exames);
                                    system("pause");
                                }
                                else if(opt2=='3'){
                                    inscrever_aluno_exame(node_exames, node_alunos);
                                    system("pause");
                                }
                                else if(opt2=='4'){
                                    remove_aluno_exame(node_exames);
                                    system("pause");
                                }
                                else if(opt2=='5'){
                                    listar_alunos_inscritos(node_exames);
                                    system("pause");
                                }
                                else if(opt2=='6'){
                                    apagar_exames(node_exames);
                                    system("pause");
                                }
                                else{
                                    opt2='0';
                                    system("CLS");
                                    printf("entrou no else");
                                    system("pause");
                                }
                        }


                    break;
                case '3':
                     while(opt2 == '0'){
                            system("CLS");
                            printf("\tPretende:\n\n");
                            printf("\t1 - Criar dados da disciplina\n");
                            printf("\t2 - Alterar dados da disciplina\n");
                            printf("\t3 - Apagar dados da disciplia\n");
                            ler_string(str,size);
                            fflush(stdin);
                            if(str[1]!='\0')
                                opt2='a';

                            else
                                opt2=str[0];



                                if(opt2=='1'){
                                    insere_disciplina(node_disciplinas);
                                    system("pause");
                                }
                                else if(opt2=='2'){

                                    char disc[10];
                                    system("cls");
                                    printf("Insira o nome da disciplina a alterar\n");
                                    fflush(stdin);
                                    ler_string(disc, 10);
                                    if(verifica_disciplina(node_disciplinas, disc)){/*CONFIRMAR SE O NUMERO EXISTE*/
                                        remove_disciplina(node_disciplinas, disc);
                                        insere_disciplina(node_disciplinas);
                                        printf("\nOs dados da disciplina foram alterados com sucesso");
                                    }
                                    else{
                                        system("cls");
                                        printf("A disciplina que inseriu não existe");
                                    }

                                    system("pause");
                                }
                                else if(opt2=='3'){
                                    char disc[10];
                                    system("cls");
                                    printf("Insira o numero do aluno que pretende remover:\n");
                                    ler_string(disc, 10);
                                    if(verifica_disciplina(node_disciplinas, disc)){/*verificar se a disciplina existe*/
                                        remove_disciplina(node_disciplinas, disc);
                                        printf("\nO aluno foi removido com sucesso\n");
                                    }
                                    else{
                                        system("cls");
                                        printf("O numero do aluno que inseriu não existe\n");
                                        }

                                    system("pause");
                                }
                                else{
                                    opt2='0';
                                    system("CLS");
                                    printf("entrou no else");
                                    system("pause");
                                }
                        }

                    break;
                case '0':
                    carrega_ficheiro_alunos(node_alunos);
                    carrega_ficheiro_disciplina(node_disciplinas);
                    /*meter as listas para ficheiros*/
                    libera_alunos(node_alunos);
                    libera_disciplinas(node_disciplinas);
                    libera_exames(node_exames);
                    exit(1);
                    break;

                default:

                    system("CLS");
                    break;
                 }
    }
}


int main()
{
    setlocale(LC_ALL,"Portuguese");
    char opt;
    escolher_opt(&opt);
    return 1;
}
