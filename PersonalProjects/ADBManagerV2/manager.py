from PyQt5.QtWidgets import QApplication, QLabel
import os


def devices():
    command = os.popen('adb devices | head -2 | tail -1 | sed -e \'s/device//g\'')
    return command.read()


def main():
    app = QApplication([])
    label = QLabel("Hello, World!")
    label.show()
    app.exec_()


main()
