#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#define ErrExit(A) fprintf(stderr, A); exit(0)

pthread_t my_thread;

void *thread_fct (void *arg) { 
  char *p;
  p = (char *)malloc(64*sizeof(char));
  strcpy(p, "\nThis is the string initialized by the worker thread\n");
//  return (void *)p;
  return NULL;
}


int main (int argc, char *argv[]) {
  int status;
  void *P; 
  status = pthread_create (&my_thread, NULL, thread_fct, NULL);
  if (status != 0) { ErrExit("Error : Create thread"); }

  status = pthread_join (my_thread, &P);
  if (status != 0) { ErrExit("Error : Join thread"); }
  printf("%s", (char *)P);
  free(P);

  return 0;
}
