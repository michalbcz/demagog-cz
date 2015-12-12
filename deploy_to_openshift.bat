@echo off

cd demagog

echo [info] running: play clean compile stage
call play clean compile stage

cd ..

echo [info] removing old deploy repo
rd /S /Q ..\demagog-openshift-deploy

echo "[info] init new deploy repo"
md ..\demagog-openshift-deploy
pushd ..\demagog-openshift-deploy
git init .
git remote add openshift ssh://566c2e6589f5cffb1900004b@overto-demagogcz.rhcloud.com/~/git/overto.git/

popd

echo "[info] copying new deploy repo content"
md ..\demagog-openshift-deploy\demagog
md ..\demagog-openshift-deploy\.openshift
xcopy demagog\*.* ..\demagog-openshift-deploy\demagog /H /S /E /K /Q
xcopy .openshift\*.* ..\demagog-openshift-deploy\.openshift /H /S /E /K /Q

echo "[info] remove .gitignores"
del /s .gitignore
cd ..\demagog-openshift-deploy

echo "[info] running: git add . -A"
git add . -A

echo "[info] running: git commit -m \"deployment\""
git commit -m "deployment"

echo "[info] make action hooks executable"
git update-index --chmod=+x .openshift/action_hooks/build
git update-index --chmod=+x .openshift/action_hooks/deploy
git update-index --chmod=+x .openshift/action_hooks/load_config
git update-index --chmod=+x .openshift/action_hooks/post_deploy
git update-index --chmod=+x .openshift/action_hooks/pre_build
git update-index --chmod=+x .openshift/action_hooks/start
git update-index --chmod=+x .openshift/action_hooks/stop
git add . -A
git commit -m "action hooks executable"

echo "[info] running: git push openshift"
git push openshift --force

popd