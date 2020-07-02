#ifndef MANIPULA_LISTAS_H_INCLUDED
#define MANIPULA_LISTAS_H_INCLUDED

void RetiraEnter(char *str);
int lista_vazia_alunos(Lista_alunos *li);
int lista_vazia_disciplinas(Lista_disciplinas *li);
int lista_vazia_exames(Lista_exames *li);

int libera_alunos(Lista_alunos *node);
int libera_disciplinas(Lista_disciplinas *node);
int libera_exames(Lista_exames *node);

int remove_aluno(Lista_alunos *node, int num);
int insere_lista_ordenada(Lista_alunos *node);
int verifica_aluno(Lista_alunos *node, int num);


int insere_disciplina(Lista_disciplinas *node);
int remove_disciplina(Lista_disciplinas *node, char *nome);
int verifica_disciplina(Lista_disciplinas *node,char *str);

int criar_exame(Lista_exames *node, Lista_disciplinas *node_disciplinas);
int listar_exames(Lista_exames *node);
int apagar_exames(Lista_exames *node);
int inscrever_aluno_exame(Lista_exames *node, Lista_alunos *node_al);
int listar_alunos_inscritos(Lista_exames *node);
int remove_aluno_exame(Lista_exames *node);


int verificar_exame(Lista_exames *node, char *disci, int epo);
int verifica_aluno_em_exame(Lista_exames *node, char *disci, int epo, int num);


void ler_data(Data *d);
void imprimir_data(Data *d);
void imprimir_epoca(int n);
void imprimir_hora(Hora *d);
void ler_hora(Hora *h);
int comparacao_data(Data a, Data b);
int comparacao_hora(Hora hor, int h, int m);


#endif // MANIPULA_LISTAS_H_INCLUDED
