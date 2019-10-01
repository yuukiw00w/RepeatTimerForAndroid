rootDir=`git rev-parse --show-toplevel`
cd $rootDir

git config --unset commit.template
rm .git/hooks/commit-msg
