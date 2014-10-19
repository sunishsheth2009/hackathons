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

print(FBLogin())

endOfPython
endfunction

function! FBShare(lineNum)
python << endOfPython

from fbvim import fb_share

token = 'n/a'

vim_buff = list(vim.current.buffer)
vim_line = vim_buff[int(vim.eval("a:lineNum")) - 1]

fb_share(token, vim_line)

endOfPython
endfunction

" --------------------------------
"  Expose our commands to the user
" --------------------------------
command! FBLogin call FBLogin()
command! -nargs=1 FBShare call FBShare(<f-args>)
