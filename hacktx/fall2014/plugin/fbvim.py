import facebook
import time
import threading

update_stop = threading.Event()
meta_sema = threading.Semaphore(1)
meta_file_name = ""
user_token = 'n/a'
graph = facebook.GraphAPI(user_token)

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
            meta_file.write(str(line_num) + ',' + str(status_id) + ',' + str(remote_likes) + '\n')
        meta_file.close()
        # update_stop.wait(3)
 
def write_meta_file(entry, perm='a'):
    meta_sema.acquire()
    meta_file = open(meta_file_name, perm)
    meta_file.write(str(entry) + '\n')
    meta_file.close()
    meta_sema.release() 
