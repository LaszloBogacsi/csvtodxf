# csv to dxf

This tool converts a list of comma separated coordinate pairs and point numbers to a dxf drawing.


**CSV com.csvtodxf.file structure:**  
P,E,N,H,C

- `P` - point number/name] 
- `E`, `N` - **E**asting, **N**orthing coordinates
- `H` - **H**eight (optional), (0.0 by default)
- `C` - **C**ode (optional)

command example:  
`$  csvtodxf <path-to-input-filename-wo-extension> <path-to-output-filename-wo-extension>`

- `- help` print this help
- input filename - the path to input com.csvtodxf.file [ MANDATORY ]  
- path to output filename [ MANDATORY ]  
- `-h n.n` text height in drawing units [default: 0.2] [ OPTIONAL ]  
- `-s n.n` scale [default: 1] [ OPTIONAL ]
- `-lc` plot all to one common layer [default is to plot all to separate layer]
- `-3d` plot 3 dimensional coordinates (X,Y,Z) [default is 2D (X,Y) ] 

Interactive questions:  
- should plot height: user input [Y/n] default yes  
- should plot coordinates: user input: [Y/n] default yes  
- should print code? user input: [y/N] default no


Features:

Currently it's a command line app [WIP]
Planning to add a GUI to interact with the app. 
