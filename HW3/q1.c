// Mustafa Aydogan 191101002

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct{
    int day;
    int month;
    int year;
} date;

typedef struct{
    int id;
    char firstname[50];
    char surname[50];
    date ind;
    date outd;
    int roomno;
} reservation;

reservation *readReservationFile(){
    char *filename = "reservations.txt";
    FILE *fp = fopen(filename, "r");
    char* token;
    int count = 0;

    if (fp == NULL){
        printf("Error: could not open file %s", filename);
        return NULL;
    }
    
    // reading line by line, max 256 bytes
    const unsigned MAX_LENGTH = 256;
    char buffer[MAX_LENGTH];

    while(fgets(buffer, MAX_LENGTH, fp)){
        count++;
    }

    fclose(fp);
    fp = fopen(filename, "r");

    reservation *reservations = malloc(sizeof(reservation) * count);

    for(int i=0; i< count-1; i++){
        fgets(buffer, MAX_LENGTH, fp);

        token = strtok(buffer, "\t");
        reservations[i].id = atoi(token);
        token = strtok(NULL, "\t");
        strcpy(reservations[i].firstname, token);
        token = strtok(NULL, "\t");
        strcpy(reservations[i].surname, token);
        token = strtok(NULL, " ");

        reservations[i].ind = *(date*) malloc(sizeof(date));

        reservations[i].ind.day = atoi(token);
        token = strtok(NULL, " ");
        reservations[i].ind.month = atoi(token);
        token = strtok(NULL, "\t");
        reservations[i].ind.year = atoi(token);
        token = strtok(NULL, " ");

        reservations[i].outd = *(date*) malloc(sizeof(date));

        reservations[i].outd.day = atoi(token);
        token = strtok(NULL, " ");
        reservations[i].outd.month = atoi(token);
        token = strtok(NULL, "\t");
        reservations[i].outd.year = atoi(token);
        token = strtok(NULL, "\t");
        reservations[i].roomno = atoi(token);
        
    }

    // close the file
    fclose(fp);

    return reservations;
}

int main(int argc, char const *argv[])
{
    reservation *reservations = readReservationFile();

    for(int i=0; i< 10; i++){
        printf("%d\t%s\t%s\t%d %d %d\t%d %d %d\t%d\n",  
            reservations[i].id, reservations[i].firstname, reservations[i].surname,
            reservations[i].ind.day, reservations[i].ind.month, reservations[i].ind.year,
            reservations[i].outd.day, reservations[i].outd.month, reservations[i].outd.year, reservations[i].roomno);
    }

    return 0;
}
