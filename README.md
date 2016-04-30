# NeuralNetwork-Backpropagation
A custom Neural Network program created in Java.
Using the backpropagation learning algorithm. Input, Hidden and Output layers and nodes can be scaled simply.

## Info
Right not it is very basic, but with added enhancements timings could be improved.

This book chapter was referenced:  
https://web.archive.org/web/20150317210621/https://www4.rgu.ac.uk/files/chapter3%20-%20bp.pdf

The calculations used come from this book.

## Epoch calculations
It took 291 epoch iterations for one entry:  
```sh
**Final epoch iteration**  
Epoch #291  
Target Value:       1.000000  
Output Node Value:  0.950049  
Difference:     0.049951  
```

When we have a batch of four entries per epoch it gives the following results for each set:  

**AND**  
54063  
27708  
403987   
27691  
451407  
30653  

**OR**  
8111  
5373  
114112  
31513  
8524  
8524  
 
**XOR**  
260432  
NA  
NA  
10530815  
NA  
NA  
811641  
