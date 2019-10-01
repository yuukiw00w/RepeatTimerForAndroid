rootDir=`git rev-parse --show-toplevel`
cd $rootDir
git config commit.template $rootDir/CommitSettings/commitTemplate.txt

mkdir -p .git/hooks
cp $rootDir/CommitSettings/commit-msg.rb $rootDir/.git/hooks/commit-msg
chmod +x $rootDir/.git/hooks/commit-msg
