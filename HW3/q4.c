// Mustafa Aydogan 191101002

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct{
    int score;
    char name[50];
} Player;

int main(int argc, char const *argv[])
{
    char filename[50];

    printf("Enter a filename: ");
    scanf("%s", filename);

    FILE *fp = fopen(filename, "r");
    char* token;
    int count = 0;

    if (fp == NULL){
        printf("Error: could not open file %s", filename);
        return 1;
    }

    // reading line by line, max 256 bytes
    const unsigned MAX_LENGTH = 256;
    char buffer[MAX_LENGTH];
    char first[MAX_LENGTH];
    while(fgets(buffer, MAX_LENGTH, fp)){
        count++;
    }

    fclose(fp);

    fp = fopen(filename, "r");

    Player *players = malloc(sizeof(Player) * count);

    fgets(first, MAX_LENGTH, fp); // to pass first line

    int score = 0;

    printf("Enter a score: ");
    scanf("%d", &score);

    for(int i=0; i< count-1; i++){
        fgets(buffer, MAX_LENGTH, fp);

        token = strtok(buffer, "\t");
        strcpy(players[i].name, token);
        token = strtok(NULL, "\t");
        players[i].score = atoi(token);
    }
    
    // close the file
    fclose(fp);

    fp = fopen("updated_scores_q4.txt", "w");

    fprintf(fp, "%s", first);

    for(int i=0; i < count-1; i++){

        if (players[i].score  != score){
            fprintf(fp, "%s\t%d\n", players[i].name, players[i].score);
        }

        
    }

    fclose(fp);
    

    return 0;
}


