#!/usr/bin/python
# -*- coding: utf-8 -*-

# test line
#
#   sur tte les positions
#     sur tout les mots
#       si le mot match
#         si test dessous
#           return ok
#
#   rien n'a match -> return faux

from words import words
from array import *
import random

grid = [
    ['.','.','.','.','.','.','.',],
    ['.','.','.','.','.','.','.',],
    ['.','.','.','.','.','.','.',],
    ['.','.','.','.','.','.','.',],
    ['.','.','.','.','.','.','.',],
    ['.','.','.','.','.','.','.',],
    ['.','.','.','.','.','.','.',]
    ]

def print_grid():
    for r in grid:
        print '\n|',
        for c in r:
            print c, '|',


def set_h(row, col, word):
    for i in range(len(word)):
        grid[row][col + i] = word[i]

def set_v(row, col, word):
    for i in range(len(word)):
        grid[row + i][col] = word[i]

def set_black_h(row, space):
    for i in range(len(space)):
        if space[i] != '.':
            if i - 1 >= 0 and space[i - 1] == '.': grid[row][i - 1] = '#'
            if i + 1 < len(space) - 1 and space[i + 1] == '.': grid[row][i + 1] = '#'

def set_black_v(col, space):
    for i in range(len(space)):
        if space[i] != '.':
            if i - 1 >= 0 and space[i - 1] == '.': grid[i - 1][col] = '#'
            if i + 1 < len(space) - 1 and space[i + 1] == '.': grid[i + 1][col] = '#'

def get_h(row, col):
    space = ''
    for i in range(7-col):
        space += grid[row][col + i]
    return space

def get_v(row, col):
    space = ''
    for i in range(7-row):
        space += grid[row + i][col]
    return space
    
def remove_non_ascii(word):
    out = ''

    for c in word:
        if c == u'â' or c == u'à': out += 'a'
        elif c == u'é' or c == u'è' or c == u'ê': out += 'e'
        elif c == u'û' or c == u'ù': out += 'u'
        elif c == u'ï' or c == u'î': out += 'i'
        elif c == u'ö' or c == u'ô': out += 'o'
        elif c == u'ç': out += 'c'
        else: out += c

    return out

def get_random(space):
    print 'get random for ', space

    for l in range(7, 0, -1):

        if l < 7 and space[l] != '.':
            continue

        start_pos = random.randint(0, len(words[l]))
        pos = start_pos + 1
        print 'start on: ', words[l][pos]

        while True:
            for i in range(len(words[l][pos])):
                try:
                    if space[i] != '.' and space[i] != words[l][pos][i]:
                    #print 'wrong: ', space, ', ', words[l][pos]
                        break
                except:
                    print 'except for -> i: ', i, ', pos: ', pos, 'len: ', l
                    print 'space: ', space
                    print 'word: ', words[l][pos]
                    quit()
                
                if i == l - 1:
                    print 'found: ', space, words[l][pos], '\n'
                    return remove_non_ascii(words[l][pos])
         
            pos = pos < len(words[l]) - 1 and pos + 1 or 0

            if pos == start_pos:
                break;

    print 'not found\n'


def word_fit(word, space):
    if len(word) > len(space): return False

    try:
        for i in range(len(word)):
            if space[i] != '.' and space[i] != word[i]:
                return False
        return True
    except:
        print_grid()
        print word
        print space
        quit()


def fill(row, col, h):
#    if row > 6 or col > 6: return True

    print_grid()
    print 'fill: ', row, ', ', col, ', ', h
    
    space = get_h(row, col)

    for l in range(len(space), 0, -1):
        for w in range(0, len(words[l])):
            if word_fit(words[l][w], space):
                if (h): set_h(row, col, words[l][w])
                else: set_v(row, col, words[l][w])

                if (not h and row == 6): return True
                if (h and col == 6): return True

                if (not h): fill(row + 1, 0, not h)
                else: fill(0, col + 1, not h)
            else:
                if (h and col == 6): return False
                if (not h and row == 6): return False

                if (h): fill(row, col + 1, h)
                else: fill(row + 1, col, h)


#    word = get_random(space)
#    if word != None:
#        set_h(i, 0, word)




for i in range(0, 3):
    grid[random.randint(0, 3)][random.randint(0, 3)] = '#'
    grid[random.randint(0, 3)][random.randint(3, 6)] = '#'
    grid[random.randint(3, 6)][random.randint(0, 3)] = '#'
    grid[random.randint(3, 6)][random.randint(3, 6)] = '#'


fill(0, 0, True)


# Set first line & column

#for i in range(0, 7, 2):
#    space = get_h(i, 0)
#    word = get_random(space)
#    if word == None:
#        set_black_h(i, space)
#    else:
#        set_h(i, 0, word)
#
#    space = get_v(0, i)
#    word = get_random(space)
#    if word == None:
#        set_black_v(i, space)
#    else:
#        set_v(0, i, word)


#set_h(3, 0, get_random(get_h(3, 0)))
#set_h(5, 0, get_random(get_h(5, 0)))


#set_v(0, 3, get_random(get_v(0, 3)))
#set_v(0, 5, get_random(get_v(0, 5)))

#for i in range(5):
#    grid[random.randint(0, 6)][random.randint(0, 6)] = random.choice('abcdefghijlmnopqrstu')
