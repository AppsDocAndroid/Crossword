#!/usr/bin/python
# -*- coding: utf-8 -*-

from Tkinter import *
 
######################################
##### configuration de la grille #####
######################################
 
######################################
# l'utilisateur rentre la taille de la grille
nbligne=input("nombre de lignes : ")
nbcolonne=input("nombre de colonnes : ")
# et le nombre de tentative de grille
nbtest=input("nombre de tentative : ")
 
# liste des cases ligne par ligne
caseligne=[]
for i in range(nbligne):
    caseligne.append([])
    for j in range(nbcolonne):
        caseligne[i].append(1)
# liste des cases colonne par colonne
casecolonne=[]
for i in range(nbcolonne):
    casecolonne.append([])
    for j in range(nbligne):
        casecolonne[i].append(1)
 
######################################
# l'utilisateur va rentrer les cases noires
fen=Tk()    # pour cela on va utiliser Tkinter comme interface
case=[]
for i in range(nbligne):
    case.append([])
    for j in range(nbcolonne):
        case[i].append(Frame(fen, bg="white", width=10, height=10))
        # fonction remplir la case ligne i colonne j
        def action (envent, li=i, co=j):
            case[li][co].configure(bg="black")
            caseligne[li][co]=0            # 0 dans les listes pour indiquer
            casecolonne[co][li]=0          # que l'on ne peut pas ecrire dedans
        case[i][j].grid(row=i, column=j, padx=2, pady=2)
        case[i][j].bind('<Button-1>', action)
# quand c'est fini on detruit la fenetre
Button(fen,text="OK", command=fen.destroy).grid(row=nbligne, column=1,columnspan=nbcolonne)
 
fen.mainloop()
 
 
 
#####################################################
##### nombre de lettres des mots dans la grille #####
#####################################################
 
#####################################################
# creons maintenant de nouvelles listes ou l'on entrera le nombre de lettre de chaque mot
nblettreligne=[]
for i in caseligne:
    nblettreligne.append(i)
nblettrecolonne=[]
for i in casecolonne:
    nblettrecolonne.append(i)
 
#####################################################
# entrons le nombre de lettre
for i in range(nbligne):            # pour chaque ligne
    j=0                             # partons de la premiere case
    while j<len(nblettreligne[i]):  # tant que j n'est pas la derniere case de la ligne
        if nblettreligne[i][j]!=0:   # si on peut ecrire dedans
            if j!=len(nblettreligne[i])-1:      # et si on est pas la derniere case de la ligne (ya repetition la non? osef ca marche c le principal)
                if nblettreligne[i][j+1]!=0:    # si l'on peut ecrire dans la case suivante (et oui c pour ca qu'il ne fallait pas etre a la derniere case)
                    nblettreligne[i][j]=nblettreligne[i][j]+nblettreligne[i][j+1]  # on ajoute a la case ou l'on est le nombre de lettre de la case suivante (hum pas tres clair tout ca, mais comme dit plus haut osef ca marche)
                    del(nblettreligne[i][j+1])  # et on supprime la case suivant (enfin on la supprime pas mais on en a tirer les info qu'on voulait donc on en a plus besoin donc on la jarte CQFD)
                    j=j-1           # case precedente
        j=j+1                       # case suivante
# meme chose pour chaque colonne
for i in range(nbcolonne):
    j=0
    while j<len(nblettrecolonne[i]):
        if nblettrecolonne[i][j]!=0:
            if j!=len(nblettrecolonne[i])-1:
                if nblettrecolonne[i][j+1]!=0:
                    nblettrecolonne[i][j]=nblettrecolonne[i][j]+nblettrecolonne[i][j+1]
                    del(nblettrecolonne[i][j+1])
                    j=j-1
        j=j+1
 
 
 
#####################################################
# on est a present en possession de:
# nbligne         -> le nombre de ligne de la grille (jure j'aurais jamais cru)
# nbcolonne       -> le nombre de colonne de la grille (etonnant ca aussi)
# caseligne       -> chaque case ligne par ligne
# casecolonne     -> chaque case colonne par colonne
#      -> 1 = on peut ecrire dedans
#      -> 0 = case noire , on ne peut pas ecrire dedans
# nblettrelibne   -> chaque mot est remplace par son nombre de lettre, classe par ligne
# nblettrecolonne -> chaque mot est remplace par son nombre de lettre, classe par colonne
 
#################################
##### generation de grilles #####
#################################
# c bien beau tout ce qu'on a fait mais il va bien faloir commencer a les creer ces grilles
 
#################################
import mot_tri      # on importe tous les mots sous forme de liste
                    # dans mot_tri, il y a une liste s'appelant mot
                    # cette liste contient des listes tel que:
                    # mot[i] est une liste contenant des mots de i littres
from random import randrange
 
grillebien=[]
 
 
for i in range(nbtest):
    grille=[]
    for j in range(nbligne):
        grille.append([])
        for k in nblettreligne[j]:
            if k==0:
                grille[j].append(0)
            else:
                mot=mot_tri.mot[k][randrange(1,len(mot_tri.mot[k]))]
                for l in range(len(mot)):
                    grille[j].append(mot[l])
 
    grille2=[]
    for j in range(nbcolonne):
        grille2.append([])
        for k in range(nbligne):
            grille2[j].append(grille[k][j])
 
    ##### verification de l'existance des mots #####
    mot=[]
    for j in grille2:
        m=""
        for k in j:
            if k==0:
                mot.append(m)
                m=""
            else:
                m=m+k
        mot.append(m)
    t=1
    for j in mot:
        if j in mot_tri.mot[len(j)]:
            1
        else:
            t=0
    if t==1:
        grillebien.append(grille2)
        print grille2
        print ""
 
 
print "fin"
