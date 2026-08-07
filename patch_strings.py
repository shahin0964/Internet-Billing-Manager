import re

with open("app/src/main/res/values/strings.xml", "r") as f:
    content = f.read()
content = content.replace('<string name="inactive_susp">Inactive/Susp</string>', '<string name="inactive_susp">Monthly Bill</string>')
with open("app/src/main/res/values/strings.xml", "w") as f:
    f.write(content)

with open("app/src/main/res/values-bn/strings.xml", "r") as f:
    content = f.read()
content = content.replace('<string name="inactive_susp">নিষ্ক্রিয়/স্থগিত</string>', '<string name="inactive_susp">মাসিক বিল</string>')
with open("app/src/main/res/values-bn/strings.xml", "w") as f:
    f.write(content)
