### Open-SynSem

Semantic Interpretation with OpenCCG and Hybrid Logic.

### [Natural Language Understanding Reading Group](http://nlu-rg.ru)

See [Materials](http://nlu-rg.ru/materials) page at NLU/RG site for the discussion (in Russian).

### Credits

 - OpenCCG by J. Baldrige, M. White et al.
 - Hybrid Logic Model Checker by L. Dragone.
 - MCFull algorithm by M. Franceschet and M. de Rijke.
 - Moloko grammar by Geert-Jan M. Kruijff and Sabrina Wilske (DFKI).
 - Textbooks by P. Blackburn and J. Bos.
 - Discussion with NLU/RG participants.
 - Kind supervision by O. Mitrofanova (St. Petersburg State University).
 - Inspired by [Mathlingvo](http://www.mathlingvo.ru).


----------


#### A note on building with Maven 3

There's a problem with the apporach I use to install jars to a local ``runtime'' repository during compilation,
which is apparently due to the way Maven 3 does caching. In case there's a problem here's how to work around:

> $ mvn validate install -U

Which means, first install the jars and then compile and install the module jar with updates forced.
Normally with maven2 ``$ mvn install'' works smoothly.
