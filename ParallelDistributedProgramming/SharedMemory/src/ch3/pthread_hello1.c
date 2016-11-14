#include <stdio.h>
#include <pthread.h>

#define NTHREADS 4

pthread_t hello_ids[NTHREADS];

void *hello_thread(void *arg){
    printf("Hello Thread\n");
    return NULL;
}


int main(int argc, char *argv[]) {
    int i, status;

    for(i=0; i<NTHREADS; i++)
        pthread_create(&hello_ids[i], NULL, hello_thread, NULL);

    for(i=0; i<NTHREADS; i++)
        pthread_join (hello_ids[i], NULL);

    printf("Main Thread: joined\n");
    return 0;
}