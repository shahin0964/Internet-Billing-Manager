import os
import re

directories = ['app/src/main/java/com/example/ui/screens/', 'app/src/main/java/com/example/ui/components/']
kt_files = []
for d in directories:
    for root, _, files in os.walk(d):
        for file in files:
            if file.endswith(".kt"):
                kt_files.append(os.path.join(root, file))

for file_path in kt_files:
    with open(file_path, "r") as f:
        content = f.read()

    # We will use regex to find Surface(...) blocks
    # It's tricky to parse nested brackets with regex.
    # Instead, we can just look for 'shape = RoundedCornerShape' inside a Surface block
    # Actually, a simpler way is to replace 'shape = RoundedCornerShape(X.dp),'
    # with 'shape = RoundedCornerShape(X.dp),\nshadowElevation = X.dp,'
    # But we want different elevations based on clickable or not.

    # Let's print out all Surface instantiations to see them
