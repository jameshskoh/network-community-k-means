# network-community-k-means
 A k-means with simulated annealing algorithm used to discover communities in a network.



## Analysis

This algorithm is a slight modification of the SADI-based algorithm from Detecting community structure in complex networks using simulated annealing with $k$-means algorithm, Jian Liu, Tingzhan Liu. In the perturbation step, instead of picking the "weakest" community to split or delete, this algorithm randomly (with 1/2, 1/3 and 1/6 chance for the weakest, 2nd weakest, 3rd weakest respectively) select 1 out of the 3 "weakest" communities.



This modification yields an improved result over Liu and Liu, and is more resilient in searching for better optima for larger datasets. The largest dataset tested has ~5,000 nodes and ~100,000 edges and takes about 2 hours to compute.



This algorithm relies on a distance metric that requires solving a lot of large system of equations. This turn out to be the major bottleneck of runtime and memory resource. Runtime and memory requirements may improve if this algorithm can rely on a less resource intensive distance metric.



The runtime complexity is $O(n^4)$, where $n$ is the number of nodes in the network.



## Data Pre-processing

1. As this algorithm is distance-based, any node must be reachable by any other nodes, else their distance would be undefined.
   1. You can do preprocessing on your network data by running BFS through it to collect all (if more than 1) fully connected subsets. You can then run the subsets through this program separately.

2. Due to the nature of this implementation, the program requires that for a network with $n$ nodes, node labels have to be in [0, n-1], with no unused labels in between. If labelled differently, `IndexOutOfBoundExceptions` can occur.



## Manual

1. Build the project as a `.jar`
2. Place `config.properties` beside the `.jar`
   1. For setting up `config.properties`, go to the next section.
3. Place your data file in `data/`
4. In command prompt, run the `.jar` file with the data file as parameter, e.g. `java -jar network-community-k-means.jar datafile.edge`
   1. For more parameters, go to the Parameters section
5. The result will be written in `data/`, e.g. `data/datafile.edge.result`
   1. A `data/datafile.edge.cache` will also be generated, see Parameters



## `config.properties` and Recommended Values

* `TMAX=2.0`, the starting temperature of simulated annealing
* `TMIN=0.1`, the ending temperature of simulated annealing
* `ALPHA=0.99`, the cooldown multiplier
* `R=50`, number of iterations before each cooldown
* `SEED=-1`, PRNG seed, for debug purpose
  * `-1` means not set
  * Set other values (e.g. `0`, `1`, ...) for reproducible result
* `PARALLEL=TRUE`, parallelization for certain parts of the program



## Parameters

>  Parameters have to follow `.jar`, and can come in any order.

* `.edge` files: the program detects `.edge` as data files
* `CACHE_ONLY`: calculates the distance metric and saves the cache **only**, simulated annealing is not run and no result is generated
* `NO_CACHE`: ignores the caches and initiates a full run, previous cache is invalidated, simulated annealing will be run and a result is generated

> When only the data file is passed in, the program will attempt to use the cache when it is in the "right shape".



## Credits

1. Data provided by
   - UCSD Software Engineering Team
     - Facebook UCSD Dataset
   - Mark Newman, University of Michigan: http://www-personal.umich.edu/~mejn/netdata/
     - Zachary's Karate Club
     - Dolphin Social Network



## Dependencies

* Matrix Toolkits Java: https://github.com/fommil/matrix-toolkits-java



## Exploration

* [ ] Implement the SADD-based algorithm by Liu and Liu

