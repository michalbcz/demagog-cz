set -o verbose;

rm -rf .git;
git init;
git remote add staging git@heroku.com:demagog-staging.git;
git remote add production git@heroku.com:demagog-production.git;

git add .;
git commit -m "deployment";

git push production master --force;


