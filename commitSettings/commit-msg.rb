#!/usr/bin/env ruby
message_file = ARGV[0]
message = File.read(message_file, :encoding => Encoding::UTF_8)

current_branch = `git branch | grep '*'`.chomp.sub('* ', '')
current_branch = current_branch << ' '
# 該当のprefixで始まるブランチはチケット番号を抜き出す
#（チケット番号以降がハイフンで句切られていない場合は保証外）
#（ハイフン以前を抜き出すので、ハイフンがなければ置き換えなし）
check_bugfix = current_branch.include?("bugfix/")
check_feature = current_branch.include?("feature/")
check_refactor = current_branch.include?("refactor/")
if check_bugfix || check_feature || check_refactor then
  check_github = current_branch.include?("#")
  if check_github then
    split_strings = current_branch.split("-", 2)
    if split_strings.length >= 2 then
      current_branch = split_strings[0] << ' '
    end
  else
    split_strings = current_branch.split("-", 3)
    if split_strings.length >= 3 then
      current_branch = split_strings[0] << '-' << split_strings[1] << ' '
    end
  end
end
new_message = message.sub(/\[issue\]/, current_branch)

File.write(message_file, new_message)
