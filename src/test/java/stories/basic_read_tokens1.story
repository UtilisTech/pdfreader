Given pdf "lipsum1.pdf"
And spacing multiplier 11

When I look at page 1
Then I get 43 lines

When I look at line 11
Then I get 11 words

When I look at the word 8
Then the word is "Cras"