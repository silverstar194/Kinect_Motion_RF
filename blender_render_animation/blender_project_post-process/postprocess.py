from PIL import Image, ImageChops, ImageColor
import sys
from os import listdir
from os.path import isfile, join
import math

def getFiles(mypath):
    return [f for f in listdir(mypath) if isfile(join(mypath, f))]

def bboxUnion(bbox1, bbox2):
    return (min(bbox1[0], bbox2[0]), min(bbox1[1], bbox2[1]), max(bbox1[2], bbox2[2]), max(bbox1[3], bbox2[3]))

def trim(im, border):
    bg = Image.new(im.mode, im.size, border)
    diff = ImageChops.difference(im, bg)
    return diff.getbbox()

def getCorrectBbox(current, required, original):
    #initialize some variables

    #required aspect ratio
    req_aspect = float(required[0]) / float(required[1])

    #width/height of original border box
    w = current[2] - current[0]
    h = current[3] - current[1]

    #center of original border box
    cx = current[0] + w/2
    cy = current[1] + h/2

    #aspect ratio of border box
    cur_aspect = float(w) / float(h)

    #if we need to add some width
    if cur_aspect < req_aspect:
        w = math.ceil(float(h) * req_aspect)
    #if we need to add some height
    elif cur_aspect > req_aspect:
        h = math.ceil(float(w) / req_aspect)

    #helping variables, half of width and height
    hw = w / 2
    hh = h / 2

    #our returning bounding box
    ret = [cx - hw, cy - hh, cx + hw, cy + hh]

    #if bounding box goes too much left
    if ret[0] < 0:
        ret[2] += ret[0]
        ret[0] = 0
    #if bounding box goes too much up
    if ret[1] < 0:
        ret[3] += ret[1]
        ret[1] = 0
    #if bounding box goes too much right
    if ret[2] > original[0]:
        ret[0] -= ret[2] - original[0]
        ret[2] = original[0]
    #if bounding box goes too much down
    if ret[3] > original[1]:
        ret[1] -= ret[3] - original[1]
        ret[3] = original[1]

    return ret


def cropAll(imgs, bbox):
    for img_name, img in imgs.iteritems():
        correct = getCorrectBbox(bbox, (640, 480), img.size)
        img = img.crop(correct)
        img = img.resize((640, 480))
        img.save(img_name)

bbox = None

def loadImages(suffix):
    ret = {}
    for img_name in map(lambda l: sys.argv[1] + "/" + suffix + "/" + l, getFiles(sys.argv[1] + "/" + suffix)):
        f = open(img_name, "rb")
        ret[img_name] = Image.open(f)
        ret[img_name].load()
        f.close()
    return ret

print("Load images")
color_images = loadImages("color")
depth_images = loadImages("depth")

print("BBox calculation")
#parse parameters, which frames to use as window
to_use_from = 0 if len(sys.argv) <= 2 else int(sys.argv[2])
to_use_to = -1 if len(sys.argv) <= 3 else int(sys.argv[3])
to_use = sorted(color_images.items())[to_use_from:to_use_to]
for img_path, img in to_use:
    t = trim(img, (0,0,0))
    bbox = t if bbox is None else bboxUnion(bbox, t)

print("Image cropping")
cropAll(color_images, bbox)
cropAll(depth_images, bbox)
