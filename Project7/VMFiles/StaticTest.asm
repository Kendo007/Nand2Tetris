@256
D=A
@SP
M=D
@Sys.init$ret.0
D=A
@SP
M=M+1
A=M-1
M=D
@LCL
D=M
@SP
M=M+1
A=M-1
M=D
@ARG
D=M
@SP
M=M+1
A=M-1
M=D
@THIS
D=M
@SP
M=M+1
A=M-1
M=D
@THAT
D=M
@SP
M=M+1
A=M-1
M=D
@5
D=A
@SP
D=M-D
@ARG
M=D
@SP
D=M
@LCL
M=D
@Sys.init
0;JMP
(Sys.init$ret.0)
// function Class1.set 0 
(Class1.set)
// push argument 0 
@ARG
D=M
@0
A=D+A
D=M
@SP
M=M+1
A=M-1
M=D
// pop static 0 
@SP
M=M-1
A=M
D=M
@Class1.0
M=D
// push argument 1 
@ARG
D=M
@1
A=D+A
D=M
@SP
M=M+1
A=M-1
M=D
// pop static 1 
@SP
M=M-1
A=M
D=M
@Class1.1
M=D
// push constant 0 
@0
D=A
@SP
M=M+1
A=M-1
M=D
// return 
@LCL
D=M
@endframe
M=D
@5
D=A
@endframe
A=M-D
D=M
@retAddr
M=D
@SP
A=M-1
D=M
@ARG
A=M
M=D
@ARG
D=M
@SP
M=D+1
@endframe
M=M-1
A=M
D=M
@THAT
M=D
@endframe
M=M-1
A=M
D=M
@THIS
M=D
@endframe
M=M-1
A=M
D=M
@ARG
M=D
@endframe
M=M-1
A=M
D=M
@LCL
M=D
@retAddr
A=M
0;JMP
// function Class1.get 0 
(Class1.get)
// push static 0 
@Class1.0
D=M
@SP
M=M+1
A=M-1
M=D
// push static 1 
@Class1.1
D=M
@SP
M=M+1
A=M-1
M=D
// sub 
@SP
M=M-1
A=M
D=M
A=A-1
M=M-D
// return 
@LCL
D=M
@endframe
M=D
@5
D=A
@endframe
A=M-D
D=M
@retAddr
M=D
@SP
A=M-1
D=M
@ARG
A=M
M=D
@ARG
D=M
@SP
M=D+1
@endframe
M=M-1
A=M
D=M
@THAT
M=D
@endframe
M=M-1
A=M
D=M
@THIS
M=D
@endframe
M=M-1
A=M
D=M
@ARG
M=D
@endframe
M=M-1
A=M
D=M
@LCL
M=D
@retAddr
A=M
0;JMP
// function Class2.set 0 
(Class2.set)
// push argument 0 
@ARG
D=M
@0
A=D+A
D=M
@SP
M=M+1
A=M-1
M=D
// pop static 0 
@SP
M=M-1
A=M
D=M
@Class2.0
M=D
// push argument 1 
@ARG
D=M
@1
A=D+A
D=M
@SP
M=M+1
A=M-1
M=D
// pop static 1 
@SP
M=M-1
A=M
D=M
@Class2.1
M=D
// push constant 0 
@0
D=A
@SP
M=M+1
A=M-1
M=D
// return 
@LCL
D=M
@endframe
M=D
@5
D=A
@endframe
A=M-D
D=M
@retAddr
M=D
@SP
A=M-1
D=M
@ARG
A=M
M=D
@ARG
D=M
@SP
M=D+1
@endframe
M=M-1
A=M
D=M
@THAT
M=D
@endframe
M=M-1
A=M
D=M
@THIS
M=D
@endframe
M=M-1
A=M
D=M
@ARG
M=D
@endframe
M=M-1
A=M
D=M
@LCL
M=D
@retAddr
A=M
0;JMP
// function Class2.get 0 
(Class2.get)
// push static 0 
@Class2.0
D=M
@SP
M=M+1
A=M-1
M=D
// push static 1 
@Class2.1
D=M
@SP
M=M+1
A=M-1
M=D
// sub 
@SP
M=M-1
A=M
D=M
A=A-1
M=M-D
// return 
@LCL
D=M
@endframe
M=D
@5
D=A
@endframe
A=M-D
D=M
@retAddr
M=D
@SP
A=M-1
D=M
@ARG
A=M
M=D
@ARG
D=M
@SP
M=D+1
@endframe
M=M-1
A=M
D=M
@THAT
M=D
@endframe
M=M-1
A=M
D=M
@THIS
M=D
@endframe
M=M-1
A=M
D=M
@ARG
M=D
@endframe
M=M-1
A=M
D=M
@LCL
M=D
@retAddr
A=M
0;JMP
// function Sys.init 0 
(Sys.init)
// push constant 6 
@6
D=A
@SP
M=M+1
A=M-1
M=D
// push constant 8 
@8
D=A
@SP
M=M+1
A=M-1
M=D
// call Class1.set 2 
@Sys.init$ret.1
D=A
@SP
M=M+1
A=M-1
M=D
@LCL
D=M
@SP
M=M+1
A=M-1
M=D
@ARG
D=M
@SP
M=M+1
A=M-1
M=D
@THIS
D=M
@SP
M=M+1
A=M-1
M=D
@THAT
D=M
@SP
M=M+1
A=M-1
M=D
@7
D=A
@SP
D=M-D
@ARG
M=D
@SP
D=M
@LCL
M=D
@Class1.set
0;JMP
(Sys.init$ret.1)
// pop temp 0  
@5
D=A
@0 
D=D+A
@SP
M=M-1
A=M
D=D+M
A=D-M
M=D-A
// push constant 23 
@23
D=A
@SP
M=M+1
A=M-1
M=D
// push constant 15 
@15
D=A
@SP
M=M+1
A=M-1
M=D
// call Class2.set 2 
@Sys.init$ret.2
D=A
@SP
M=M+1
A=M-1
M=D
@LCL
D=M
@SP
M=M+1
A=M-1
M=D
@ARG
D=M
@SP
M=M+1
A=M-1
M=D
@THIS
D=M
@SP
M=M+1
A=M-1
M=D
@THAT
D=M
@SP
M=M+1
A=M-1
M=D
@7
D=A
@SP
D=M-D
@ARG
M=D
@SP
D=M
@LCL
M=D
@Class2.set
0;JMP
(Sys.init$ret.2)
// pop temp 0  
@5
D=A
@0 
D=D+A
@SP
M=M-1
A=M
D=D+M
A=D-M
M=D-A
// call Class1.get 0 
@Sys.init$ret.3
D=A
@SP
M=M+1
A=M-1
M=D
@LCL
D=M
@SP
M=M+1
A=M-1
M=D
@ARG
D=M
@SP
M=M+1
A=M-1
M=D
@THIS
D=M
@SP
M=M+1
A=M-1
M=D
@THAT
D=M
@SP
M=M+1
A=M-1
M=D
@5
D=A
@SP
D=M-D
@ARG
M=D
@SP
D=M
@LCL
M=D
@Class1.get
0;JMP
(Sys.init$ret.3)
// call Class2.get 0 
@Sys.init$ret.4
D=A
@SP
M=M+1
A=M-1
M=D
@LCL
D=M
@SP
M=M+1
A=M-1
M=D
@ARG
D=M
@SP
M=M+1
A=M-1
M=D
@THIS
D=M
@SP
M=M+1
A=M-1
M=D
@THAT
D=M
@SP
M=M+1
A=M-1
M=D
@5
D=A
@SP
D=M-D
@ARG
M=D
@SP
D=M
@LCL
M=D
@Class2.get
0;JMP
(Sys.init$ret.4)
// label END 
(Sys.init$END)
// goto END 
@Sys.init$END
0;JMP
