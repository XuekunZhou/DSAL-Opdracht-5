# DSAL-Opdracht-5
School assignment

evalueerEpoch:
Evalueert alle kandidaatroutes in de epoch met de evalueerKandidaat method.


evalueerKandidaat:
De score wordt bepaalt door alle afstanden van de route op te tellen. Per pakketje gaat er 25 punten * (1 / positie in de route) af. Wanneer een bestemming met pakketjes al geweest is gaat er niets meer van de score af.


bepaalRoute:
Sorteert alle kandidaten in de epoch met de beste op index 0. De kandidaat wordt vergeleken met de beste route van de vorige epoch. Wanneer deze beter vervangt hij de elite. Daarnaast print hij de elite.


randomKandidaat:
Maakt een route dat alleen de bestemmingen bevat van de pakketjes. De route begint altijd op 1. De volgorde van de volgende bestemmingen zijn willekeurig.


startSituatie:
Gebruikt de randomKandidaat om een aantal random kanidaat routes te maken. 


muteer:
Voegd op een willekeurige plek in de route een willekeurige bestemming, behalve voor de eerste bestemming en achter de laatste bestemming. Dezelfde bestemmingen kunnen niet direct achter elkaar worden geplaatst.

volgendeEpoch:
Afhankelijk van de versie doet hij

  versie 2.5 (main):
  Vult de kandidaten van de volgende epoch met mutaties van de elite
  
  versie 3:
  Vult de helft van de volgende epoch kandidaten met mutaties van de beste helft van de vorige epoch en vult de rest van de kandidaten met nieuwe random routes die
  gemaakt zijn met randomKandidaat method.

