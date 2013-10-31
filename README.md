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

1. `git clone git@github.com:michalbcz/demagog-cz.git` #if you are svn refugee this is like svn checkout of repository)
+ `git checkout develop` # switch to develop branch for more info see Repository section 
below
+ `play debug` # start play with remote debug enabled (you will see remote debugging ports in console out)
+ `run` # run app on localhost:9000, you can use run <port> to change port
+ `open your browser and go to http://localhost:9000` - you should see application from standard user point of view
+ `go to http://localhost:9000/admin to see administration interface. Default
username/password in development is test/test.` On production you have to set pair of system properties -Ddemagog.defaultUsername=<username> -Ddemagog.defaultPassword=<password>
+ `make your great contribution`
+ `git commit -am "i add this new cool feature"` # -a param add all changed files to git index so you don't need to do git add before. But use it wisely :)
+ `git pull` # love not war

### Git Repository

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
+ git push (send it to remote)
        

#### Deploy to Heroku

We are hosting application on Heroku cloud. There are 2 applications:

1. **demagog-staging** - testing environment
+ **demagog-production** - production environment

##### How to deploy app to Heroku
1. Init new repository in /demagog subfolder with `git init`. This is, because Heroku expects that application 
is in root of Git repo (so we create temporary nested repository for deployment).
+ Add Heroku remotes (staging and production)
```
    git remote add staging git@heroku.com:demagog-staging.git
    git remote add production git@heroku.com:demagog-production.git
```
+ Add and commit all files (except .gitignore and .target)
+ Push application to staging and then after testing to production environment
```
    git push staging master
```
then
```
    git push production master
```

##### How works environment separation

Code in both environmens should be same. Environment configuration is selected in `Procfile` 
by parameter 
```
    -Dconfig.resource=${DEMAGOG_ENVIRONMENT}.conf
```

`DEMAGOG_ENVIRONMENT` variable is defined  for each environment. This was done at application 
creation by this command:
```
    heroku config:add DEMAGOG_ENVIRONMENT=staging --remote staging
    heroku config:add DEMAGOG_ENVIRONMENT=production --remote production
```
