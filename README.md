# PaintByNumbersGenerator

This project converts a given image to a "paint by numbers" style image.

Given an image and a number `n`, the program determines the `n` most important colors and then 
normalizes each pixel color value to the closest "important color". This reduces the image to `n` 
different colors while also keeping the highest possible fidelity under this constraint.

## How it Works

This program uses two main algorithms to determine the `n` most important colors:
- K-D Tree
- Median of Medians

### K-D Tree

This algorithm partitions kth-dimensional points into a tree by splitting a list of points into two
smaller lists containing points on either side of a chosen median point. At each level of the tree,
the algorithm determines the median based on a specific dimension value.

For example, the k-d tree algorithm given a list of 3 dimensional points `(x, y, z)` at depth 0 
would determine the median of the points via the `x` value of each point. At depth 1, the algorithm 
would use the `y` value, `z` at depth 2 and so on.

Because the **Median of Medians** algorithm is used to find the median at each level of the tree,
the time complexity for this algorithm is `O(n log n)`

### Median of Medians

The Median of Medians algorithm computes the median of a list of numbers in `O(n)` (linear) time.
This algorithm determines the median for each level of the K-D Tree based on the dimension the level
partitions on.