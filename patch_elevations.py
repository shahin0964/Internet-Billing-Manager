import os
import re

directories = ['app/src/main/java/com/example/ui/screens/', 'app/src/main/java/com/example/ui/components/']
kt_files = []
for d in directories:
    for root, _, files in os.walk(d):
        for file in files:
            if file.endswith(".kt"):
                kt_files.append(os.path.join(root, file))

def process_file(file_path):
    with open(file_path, "r") as f:
        content = f.read()

    out = []
    idx = 0
    while True:
        # Match 'Surface(' or 'Surface ('
        m = re.search(r'\bSurface\s*\(', content[idx:])
        if not m:
            out.append(content[idx:])
            break
            
        pos = idx + m.start()
        out.append(content[idx:pos])
        
        p = idx + m.end() - 1 # index of '('
        depth = 0
        end_pos = p
        while end_pos < len(content):
            if content[end_pos] == '(':
                depth += 1
            elif content[end_pos] == ')':
                depth -= 1
                if depth == 0:
                    break
            end_pos += 1
            
        if end_pos < len(content) and content[end_pos] == ')':
            surface_args = content[pos:end_pos+1]
            
            if 'RoundedCornerShape' in surface_args:
                clickable = 'onClick' in surface_args or '.clickable' in surface_args
                
                shadow_elev = "6.dp" if clickable else "3.dp"
                tonal_elev = "3.dp" if clickable else "2.dp"
                
                # Remove existing
                surface_args = re.sub(r'shadowElevation\s*=\s*[^,]+,\s*', '', surface_args)
                surface_args = re.sub(r'tonalElevation\s*=\s*[^,]+,\s*', '', surface_args)
                
                # We want to match `shape = RoundedCornerShape(...)` and insert our elevations.
                # However, there might not be a trailing comma.
                def replace_shape(match):
                    return match.group(1) + f"\nshadowElevation = {shadow_elev},\ntonalElevation = {tonal_elev}" + match.group(2)
                
                new_args = re.sub(
                    r'(shape\s*=\s*RoundedCornerShape\([^)]+\))(,?)',
                    replace_shape,
                    surface_args
                )
                # Let's fix missing comma if we added new attributes but didn't have one
                # Actually, replace_shape keeps the comma or adds one if needed?
                # No, if it didn't have a comma, and there are more args? It must be the last arg.
                # Let's do a simpler replacement: find `shape = RoundedCornerShape(...)` and append `, shadowElevation = ...`
                
                def replace_shape2(match):
                    has_comma = match.group(2) == ','
                    prefix = match.group(1)
                    if has_comma:
                        return f"{prefix},\nshadowElevation = {shadow_elev},\ntonalElevation = {tonal_elev},"
                    else:
                        # If it was the last arg without comma
                        return f"{prefix},\nshadowElevation = {shadow_elev},\ntonalElevation = {tonal_elev}"
                        
                new_args = re.sub(
                    r'(shape\s*=\s*RoundedCornerShape\([^)]+\))(,?)',
                    replace_shape2,
                    surface_args
                )
                
                out.append(new_args)
            else:
                out.append(surface_args)
                
            idx = end_pos + 1
        else:
            out.append(content[pos:pos+8])
            idx = pos + 8

    new_content = "".join(out)
    if new_content != content:
        with open(file_path, "w") as f:
            f.write(new_content)
        print(f"Updated {file_path}")

for file_path in kt_files:
    process_file(file_path)
