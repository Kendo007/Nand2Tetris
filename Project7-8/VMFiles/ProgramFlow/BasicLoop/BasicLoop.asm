// push constant 0     
@0    
D=A
@SP
M=M+1
A=M-1
M=D
// pop local 0          
@LCL
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
// push local 0 
@LCL
D=M
@0
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
// pop local 0	         
@LCL
D=M
@0	        
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
// if-goto LOOP  
@SP
M=M-1
A=M
D=M
@Sys.init$LOOP
D;JNE
// push local 0         
@LCL
D=M
@0        
A=D+A
D=M
@SP
M=M+1
A=M-1
M=D
