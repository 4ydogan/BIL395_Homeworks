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
    char *first;
    int count = 0;

    if (fp == NULL){
        printf("Error: could not open file %s", filename);
        return 1;
    }

    // reading line by line, max 256 bytes
    const unsigned MAX_LENGTH = 256;
    char buffer[MAX_LENGTH];

    while(fgets(buffer, MAX_LENGTH, fp)){
        count++;
    }

    fclose(fp);
    fp = fopen(filename, "r");

    Player *players = malloc(sizeof(Player) * count);

    first = fgets(buffer, MAX_LENGTH, fp); // to pass first line

    for(int i=0; i< count-1; i++){
        fgets(buffer, MAX_LENGTH, fp);

        token = strtok(buffer, " ");
        strcpy(players[i].name, token);
        token = strtok(NULL, "\t");
        players[i].score = atoi(token);
    }

    Player temp;

    for (int i = 0; i < count-2; i++)
    {
        for (int j = i+1; j > 0; j--)
        {
            if (players[j-1].score < players[j].score)
            {
                temp = players[j];
                players[j] = players[j-1];
                players[j-1] = temp;
            }
        }
    }

    int k = 0;

    printf("Enter an integer: \n");
    scanf("%d", &k);

    for(int i=0; i < k; i++){
        printf("%s\t%d\n", players[i].name, players[i].score);
    }

    // close the file
    fclose(fp);

    return 0;
}
