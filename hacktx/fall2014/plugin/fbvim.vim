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
execute "set scrollbind"
execute "12vsplit .like_buffer"
execute "set scrollbind"
execute "normal \<C-w>\<C-w>"
python << endOfPython

from fbvim import fb_login

fb_login(str(vim.eval('expand("%:t")'))) # for file name only: .split('.')[0]

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

from fbvim import fb_share, write_meta_file

vim_buff = list(vim.current.buffer)
line_num = int(vim.eval('a:lineNum')) - 1
vim_line = vim_buff[line_num]

print 'sharing line ' + str(line_num + 1) + ' to facebook'
status_id = fb_share(vim_line)
write_meta_file(str(line_num + 1) + ',' + str(status_id) + ',0')

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
