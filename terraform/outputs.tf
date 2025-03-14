output "instance_ip" {
  value = aws_instance.java_app.public_ip
}