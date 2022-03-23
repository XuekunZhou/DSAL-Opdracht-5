# DSAL-Opdracht-5
In de route analyse matrix excel bestand vind je een aantal matrixen met daarin de scores van de beste gevonden routes per aantal epochs en aantal kandidaten.

evalueerEpoch:
Evalueert alle kandidaatroutes in de epoch met de evalueerKandidaat method.


evalueerKandidaat:
De score wordt bepaalt door alle afstanden van de route op te tellen. Per pakketje gaat er 25 punten * (1 / positie in de route) af. Wanneer een bestemming met pakketjes al geweest is gaat er niets meer van de score af. Wanneer de route niet begint met 1 gaat er 10000 punten erbij en wanneer de pakketjes niet bezord worden omdat ze niet in de route zitten gaat er 400 punten erbij per pakketje.


bepaalRoute:
Sorteert alle kandidaten in de epoch met de beste op index 0. De kandidaat wordt vergeleken met de beste route van de vorige epoch. Wanneer deze beter vervangt hij de elite. Daarnaast print hij de elite.


randomKandidaat:
Maakt een willekeurige route dat een willekeurige grootte heeft tussen de 5 en 20 bestemmingnen.


startSituatie:
Gebruikt de randomKandidaat om een aantal random kanidaat routes te maken. 


muteer:
Voegd op een willekeurige plek in de route een willekeurige bestemming, behalve voor de eerste bestemming en achter de laatste bestemming. Dezelfde bestemmingen kunnen niet direct achter elkaar worden geplaatst.

volgendeEpoch:
Neemt de top 45% mee naar de volgende epoch en vult de rest van de kandidaten met nieuwe random kandidaten.


