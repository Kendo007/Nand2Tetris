// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, 
// the screen should be cleared.

(LISTENING)
@KBD
D=M
@WHITE
D;JEQ

@color     // -1 or 0
M=-1
@FILL
0;JMP

(WHITE)
@color
M=0

(FILL)
@SCREEN
D=A
@addr
M=D

(LOOP)
@KBD          // Filling the entire screen with color stored at @color location
D=A
@addr
D=D-M
@LISTENING
D;JEQ

@color
D=M
@addr
A=M
M=D

@addr
M=M+1
@LOOP
0;JMP