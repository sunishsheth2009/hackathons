" --------------------------------
" Add our plugin to the path
" --------------------------------
python import sys
python import vim
python sys.path.append(vim.eval('expand("<sfile>:h")'))

" --------------------------------
"  Function(s)
" --------------------------------
function! FBLogin()
python << endOfPython

from fbvim import fb_login

file_name = str(vim.eval('expand("%:t")')) # for file name only: .split('.')[0]
fb_login(file_name)

print 'waiting on app review'

endOfPython
endfunction

function! FBLogout()
python << endOfPython

from fbvim import fb_logout

print(fb_logout())

endOfPython
endfunction

function! FBShare(lineNum)
python << endOfPython

from fbvim import fb_share

token = 'n/a'

vim_buff = list(vim.current.buffer)
line_num = int(vim.eval('a:lineNum')) - 1
vim_line = vim_buff[line_num]

print 'sharing line ' + str(line_num + 1) + ' to facebook'
status_id = fb_share(token, vim_line)
file_name = str(vim.eval('expand("%:t")'))
meta_file_name = '.' + file_name + '.likes'
meta_file = open(meta_file_name, 'a')
meta_file.write(str(line_num + 1) + ',' + str(status_id) + '\n')
meta_file.close()

endOfPython
endfunction

" --------------------------------
"  Expose our commands to the user
" --------------------------------
command! FBLogin call FBLogin()
command! FBLogout call FBLogout()
command! -nargs=1 FBShare call FBShare(<f-args>)
" Make sure to end the update thread on exit
autocmd VimLeave * call FBLogout()
