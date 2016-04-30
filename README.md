# NeuralNetwork-Backpropagation
A custom Neural Network program created in Java.  
Using the backpropagation learning algorithm: Input, Hidden & Output layers & nodes can be scaled simply.

As of this moment we have 2 inputs, 2 hidden layers with 3 nodes each, and one output node.

AND, OR, & XOR are working.
Altough optimisations are needed for XOR as it takes too many iterations.

## Information
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

## Future Enhancements
### Alphabet
Create a grid (5x7) which shows a letter.  
Each cell in the grid will be on or off (0 or 1).  
I will feed this grid as inputs into the input layer.  

I will need:  
* 35 inputs
* 26 outputs
* 6 - 22 hidden nodes
* hidden layers? 1 - 4 (1 should do fine, we will use 2)

The system should be able to distuinguise letters form one another.
I should be able to then give it letters that are not so perfect (random pixel or two wrong) and it should be able to find the closest match.

### Refactor
I want to make the project more modular.
Have a system to add more hidden layers at will.

### Gaming
I want to use the current game scene as the inputs,
and the actions as outputs.

Then the algorithm can learn and get better at the game.

A grid game like snake or breakout would be good.
We could analyse the game screen, turn it into a grid positive and negative values on each square.

The algorithm should learn in breakout for instance that if the positive cell is coming towards you that you should hit it, but that by breaking a block you get rewarded.

This seems more like reinforcement learning territory.
We need explicit inputs and outputs for supervised learning.

### Image recognition
I could pass it a rastor image, with pixel values on each square.
If I pass it loads of images, of cats and dogs say, it should find patterns in the images and figure it out.



