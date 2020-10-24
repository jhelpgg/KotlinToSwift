class GenericError :  Error, CustomStringConvertible
{
     public let message : String

     init(_ message:String)
     {
          self.message = message
     }

     var description : String { return "GenericError : \(self.message)" }
}
