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
// pop pointer 1            
@3
D=A
@1           
D=D+A
@SP
M=M-1
A=M
D=D+M
A=D-M
M=D-A
// push constant 0          
@0         
D=A
@SP
M=M+1
A=M-1
M=D
// pop that 0               
@THAT
D=M
@0              
D=D+A
@SP
M=M-1
A=M
D=D+M
A=D-M
M=D-A
// push constant 1    
@1   
D=A
@SP
M=M+1
A=M-1
M=D
// pop that 1               
@THAT
D=M
@1              
D=D+A
@SP
M=M-1
A=M
D=D+M
A=D-M
M=D-A
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
// push constant 2          
@2         
D=A
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
// pop argument 0           
@ARG
D=M
@0          
D=D+A
@SP
M=M-1
A=M
D=D+M
A=D-M
M=D-A
// label LOOP 
(Sys.init$LOOP)
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
// if-goto COMPUTE_ELEMENT  
@SP
M=M-1
A=M
D=M
@Sys.init$COMPUTE_ELEMENT
D;JNE
// goto END  
@Sys.init$END
0;JMP
// label COMPUTE_ELEMENT 
(Sys.init$COMPUTE_ELEMENT)
// push that 0 
@THAT
D=M
@0
A=D+A
D=M
@SP
M=M+1
A=M-1
M=D
// push that 1 
@THAT
D=M
@1
A=D+A
D=M
@SP
M=M+1
A=M-1
M=D
// add 
@SP
M=M-1
A=M
D=M
A=A-1
M=D+M
// pop that 2 
@THAT
D=M
@2
D=D+A
@SP
M=M-1
A=M
D=D+M
A=D-M
M=D-A
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
// push constant 1 
@1
D=A
@SP
M=M+1
A=M-1
M=D
// add 
@SP
M=M-1
A=M
D=M
A=A-1
M=D+M
// pop pointer 1  
@3
D=A
@1 
D=D+A
@SP
M=M-1
A=M
D=D+M
A=D-M
M=D-A
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
// push constant 1 
@1
D=A
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
// pop argument 0           
@ARG
D=M
@0          
D=D+A
@SP
M=M-1
A=M
D=D+M
A=D-M
M=D-A
// goto LOOP 
@Sys.init$LOOP
0;JMP
// label END 
(Sys.init$END)
