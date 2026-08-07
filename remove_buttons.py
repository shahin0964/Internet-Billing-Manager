with open("app/src/main/java/com/example/ui/screens/DashboardScreen.kt", "r") as f:
    lines = f.readlines()

# Let's find the exact lines
start_idx = -1
end_idx = -1

for i, line in enumerate(lines):
    if "Spacer(modifier = Modifier.height(14.dp))" in line and "StatusBadge(status = settings.networkStatus)" in lines[i-3]:
        start_idx = i
    if start_idx != -1 and i > start_idx:
        if "                }\n" == line and "            }\n" == lines[i+1] and "        }\n" == lines[i+2]:
            end_idx = i - 1
            break

if start_idx != -1 and end_idx != -1:
    print(f"Removing lines {start_idx+1} to {end_idx+1}")
    del lines[start_idx:end_idx+1]
    with open("app/src/main/java/com/example/ui/screens/DashboardScreen.kt", "w") as f:
        f.writelines(lines)
else:
    print("Could not find lines to remove.")

