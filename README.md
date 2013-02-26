Demagog.cz Quotes Collecting And Voting Platform
================================================

Project aims to help demagog.cz (czech alternative of http://www.factcheck.org/)
to find out what kind of facts their users would like to check - "truth or not?".

We started this during [Random Hack Of Kidness hackhaton](http://www.rhok.org/) in Prague on 2013 ([#rhokprague](http://twitter.com/search?q=%23rhokprague) with theme to support projects from [#restartcesko](http://twitter.com/search?q=%23restartcesko) competition.
Demagog.cz wins 3rd place with this project.

It's a web platform which has this user flow :

1. Users gather quotes which they want to check (through basic html form or with bookmarklet just from any page they visit with their browser)
+ Admin of this application review inserted quotes (facts) and either approve the quote for voting or cancel it.
+ Approved quote is then visible on the voting page where users can vote for quotes (to show demagog.cz's team that they find it interesting enough to be checked) and share it.
+ Demagog.cz team picks that quotes with most votes, remove it from voting page and process it by their analysis team and later then publish fact check article on demagog.cz main page.

## Developer guide


### Getting started


1. git clone git@github.com:michalbcz/demagog-cz.git # if you are svn refugee this is like 
svn checkout of repository
+ git checkout develop # switch to develop branch for more info see Repository section 
below
+ pull love not war

#### Repository


We are using **git flow layout** (read readme of https://github.com/nvie/gitflow and blog post 
http://nvie.com/posts/a-successful-git-branching-model/).

Development and your pull request are done on develop branch.
Master branch should be in production quality and this version we deploy on production (or 
test) environment.

Use right commit messages format see http://git-scm.com/book/ch5-2.html#commit_guidelines.

When fixing issue use this format in commit message - 
https://github.com/blog/1386-closing-issues-via-commit-messages.

##### Typical working scenarios:

###### regular development
1. git checkout develop (ie. switch to develop branch)
+ do your development
+ commit and pull

###### releasing of production quality version (aka preparing for production/test deployment)
1. you did all the tests and you are convinced that app is in production quality
+ git flow release start <version>
+ change version in your configuration etc., make last fixes
+ git flow release finish (this cause merge your develop to master ie. production 
branch)
+ git checkout master (switch to master)
+ git pull (send it to remote)
        



        


