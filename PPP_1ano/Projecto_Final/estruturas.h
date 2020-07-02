#ifndef ESTRUTURAS_H_INCLUDED
#define ESTRUTURAS_H_INCLUDED

typedef struct aluno *Lista_alunos;
typedef struct exames *Lista_exames;
typedef struct disciplina *Lista_disciplinas;

typedef struct dados_aluno
{
    char nome[60];
    int n_aluno;
    int curso; /*LEI - 1  LDM - 2*/
    int ano_matricula;
    int regime; /*normal-0 trabalhador-1 atleta-2 dirigente associativo-3 Erasmus-4 */

}t_dados_alunos;

typedef struct aluno
{
    struct dados_aluno DADOS_ALUNO;
    struct aluno *prox;

}t_aluno;


typedef struct data
{
    int dia, mes, ano;
}Data;

typedef struct hora
{
    int h_inicio, m_inicio;
    int h_final, m_final;
}Hora;
/*EXAMES*/

typedef struct dados_exame{

    char disciplina[10];
    int epoca,numero_alunos;
    char salas[10][3];

}t_dados_exame;

typedef struct exames
{
    struct dados_exame DADOS_EXAME;
    Lista_alunos *ALUNOS_INSCRITOS;
    struct data data_exame;
    struct hora hora_exame;
    struct exames * prox;

}t_exames;


/*DISCIPLINAS*/
typedef struct dados_disciplina
{
    char nome[20];
    char nome_docente[60];

}t_dados_disciplina;

typedef struct disciplina
{
    struct dados_disciplina DADOS_DISCIPLINA;
    struct disciplina *prox;

}t_disciplina;


Lista_alunos * cria_lista_alunos();
Lista_exames * cria_lista_exames();
Lista_disciplinas * cria_lista_disciplinas();

#endif // ESTRUTURAS_H_INCLUDED
