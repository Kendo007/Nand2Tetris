// push constant 3030 
@3030
D=A
@SP
M=M+1
A=M-1
M=D
// pop pointer 0 
@3
D=A
@0
D=D+A
@SP
AM=M-1
D=D+M
A=D-M
M=D-A
// push constant 3040 
@3040
D=A
@SP
M=M+1
A=M-1
M=D
// pop pointer 1 
@3
D=A
@1
D=D+A
@SP
AM=M-1
D=D+M
A=D-M
M=D-A
// push constant 32 
@32
D=A
@SP
M=M+1
A=M-1
M=D
// pop this 2 
@THIS
D=M
@2
D=D+A
@SP
AM=M-1
D=D+M
A=D-M
M=D-A
// push constant 46 
@46
D=A
@SP
M=M+1
A=M-1
M=D
// pop that 6 
@THAT
D=M
@6
D=D+A
@SP
AM=M-1
D=D+M
A=D-M
M=D-A
// push pointer 0 
@3
D=A
@0
A=D+A
D=M
@SP
M=M+1
A=M-1
M=D
// push pointer 1 
@3
D=A
@1
A=D+A
D=M
@SP
M=M+1
A=M-1
M=D
// add 
@SP
AM=M-1
D=M
A=A-1
M=D+M
// push this 2 
@THIS
D=M
@2
A=D+A
D=M
@SP
M=M+1
A=M-1
M=D
// sub 
@SP
AM=M-1
D=M
A=A-1
M=M-D
// push that 6 
@THAT
D=M
@6
A=D+A
D=M
@SP
M=M+1
A=M-1
M=D
// add 
@SP
AM=M-1
D=M
A=A-1
M=D+M
