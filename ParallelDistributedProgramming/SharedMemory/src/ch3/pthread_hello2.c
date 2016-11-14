#include <stdio.h>
#include <pthread.h>

#define NTHREADS 4

pthread_t hello_ids[NTHREADS];
int th_ranks[NTHREADS];

void *hello_thread(void *arg){
    int rank = *(int *)arg; // read integer value
    printf ("Hello Thread %d \n", rank);
    return NULL;
}

int main(int argc, char *argv[]) {
    int i, status;

    for(i=0; i<NTHREADS; i++) {
        th_ranks[i] = i;
        pthread_create(&hello_ids[i], NULL, hello_thread, &th_ranks[i]);
    }
    for(i=0; i<NTHREADS; i++)
        pthread_join (hello_ids[i], NULL);

    printf("Main Thread: joined\n");
    return 0;
}