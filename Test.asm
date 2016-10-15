constant
argument

//add starts here 
@SP
AM=M-1
D=M
A=A-1
M=D+M
//add ends here


//sub starts here 
@SP
AM=M-1
D=M
A=A-1
M=M-D
//sub ends here


//gt starts here
@SP
AM=M-1
D=M
A=A-1
D=D-M
M=0
@END.0
D;JGE
@SP
A=M-1
M=-1
(END.0)
//gt ends here 


//lt starts here
@SP
AM=M-1
D=M
A=A-1
D=D-M
M=0
@END.1
D;JLE
@SP
A=M-1
M=-1
(END.1)
//lt ends here 


//eq starts here
@SP
AM=M-1
D=M
A=A-1
D=M-D
M=0
@END.2
D;JNE
@SP
A=M-1
M=-1
(END.2)
//eq ends here 


//and starts here 
@SP
AM=M-1
D=M
A=A-1
M=D&M
//and ends here


//neg starts here 
@SP
A=M-1
M=-M
//neg ends here


//or starts here 
@SP
AM=M-1
D=M
A=A-1
M=D|M
//or ends here


//not starts here 
@SP
A=M-1
M=!M
//not ends here

this
this

//add starts here 
@SP
AM=M-1
D=M
A=A-1
M=D+M
//add ends here


//sub starts here 
@SP
AM=M-1
D=M
A=A-1
M=M-D
//sub ends here

temp

//add starts here 
@SP
AM=M-1
D=M
A=A-1
M=D+M
//add ends here

