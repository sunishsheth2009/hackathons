import facebook
import time
import threading
import vim

update_stop = threading.Event()
meta_sema = threading.Semaphore(1)
meta_file_name = ''
user_token = 'n/a'
graph = facebook.GraphAPI(user_token)
changed = False

def fb_login(file_name):
    # TODO: LOG IN
    global meta_file_name
    meta_file_name = '.' + file_name + '.likes'
    create_file = open(meta_file_name, 'a')
    create_file.close()
    # TODO: DRAW LIKES
    threading.Thread(target=update).start()

def fb_logout():
    # TODO: LOG OUT
    update_stop.set()
    # TODO: CLEAR LIKES
    return 'waiting on fb app review'

def fb_share(vim_line):
    status_attachment = {"name": "User shared some code from Vim", "link":"http://www.vexal.us/vim/" + str(vim_line)}
    return str(graph.put_wall_post(message=vim_line, attachment=status_attachment)['id']).split('_')[1]

def get_num_likes(status_id):
    status = graph.get_object(status_id)
    # return str(len(status['likes']['data']))
    try:
        num_likes = str(len(status['likes']['data']))
    except KeyError:
        return 0
    return num_likes

def update():
    global changed
    while(not update_stop.is_set()):
        meta_file = open(meta_file_name, 'r+')
        lines = meta_file.read().splitlines()
        meta_file.close()
        meta_file = open(meta_file_name, 'r+')
        for line in lines: 
            entries = str(line).split(',')
            line_num = entries[0]
            status_id = entries[1]
            num_likes = entries[2]
            remote_likes = get_num_likes(str(status_id))
            if(remote_likes != num_likes):
                changed = True
            meta_file.write(str(line_num) + ',' + str(status_id) + ',' + str(remote_likes) + '\n')
        meta_file.close()
        if(changed):
            write_likes()
        update_stop.wait(1)
 
def write_meta_file(entry, perm='a'):
    meta_sema.acquire()
    meta_file = open(meta_file_name, perm)
    meta_file.write(str(entry) + '\n')
    meta_file.close()
    meta_sema.release() 

def write_likes():
    num_lines = len(list(vim.current.buffer))
    # vim.command("bn")
    vim_buffer = list(vim.current.buffer)
    meta_sema.acquire()
    meta_file = open(meta_file_name, 'r')
    lines = meta_file.read().splitlines()
    meta_file.close()
    meta_sema.release()
    line_likes = {}
    for line in lines:
        entries = str(line).split(',')
        line_likes[int(entries[0])] = int(entries[2])
    new_buffer = []
    for i in range(1, num_lines + 1):
        # new_buffer.append('\n' if i not in line_likes.keys() else str(line_likes[i]) + ' likes\n')
        if(i in line_likes.keys()):
            new_buffer.append(str(line_likes[i]) + ' likes\n')
        else:
            new_buffer.append('\n')
    vim.buffers[1][:] = new_buffer
    # vim.command("bn")
