Option Explicit

Dim shell, arquivos, pastaProjeto, executavel
Set shell = CreateObject("WScript.Shell")
Set arquivos = CreateObject("Scripting.FileSystemObject")

pastaProjeto = arquivos.GetParentFolderName(WScript.ScriptFullName)
executavel = pastaProjeto & "\app\HonkaiDataBank\HonkaiDataBank.exe"

If arquivos.FileExists(executavel) Then
    shell.Run Chr(34) & executavel & Chr(34), 1, False
Else
    MsgBox "Executável não encontrado: " & executavel, 16, "Honkai Data Bank"
End If
