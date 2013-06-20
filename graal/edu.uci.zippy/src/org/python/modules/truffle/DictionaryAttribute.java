package org.python.modules.truffle;

import java.util.ArrayList;

import org.python.ast.datatypes.PDictionary;
import org.python.ast.datatypes.PList;
import org.python.modules.truffle.annotations.ModuleMethod;

public class DictionaryAttribute extends Module {
    
    public DictionaryAttribute() {
        try {
            addAttributeMethods();
        } catch (NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @ModuleMethod
    public Object setDefalut(Object[] args, Object self) {        
        if (args.length == 1) {
            return setDefalut(args[0], self);
        } else if (args.length == 2) {
            return setDefalut(args[0], args[1], self);
        } else {
            throw new RuntimeException("wrong number of arguments for setdefault()");
        }
    }
    
    public Object setDefalut(Object arg, Object self) {
        PDictionary dict = (PDictionary) self;
        
        if (dict.getMap().containsKey(arg)) {
            return dict.getMap().get(arg);
        } else {
            dict.getMap().put(arg, null);
            return null;
        }
    }
    
    public Object setDefalut(Object arg0, Object arg1, Object self) {
        PDictionary dict = (PDictionary) self;
        
        if (dict.getMap().containsKey(arg0)) {
            return dict.getMap().get(arg0);
        } else {
            dict.getMap().put(arg0, arg1);
            return arg1;
        }
    }

    @ModuleMethod
    public Object pop(Object[] args, Object self) {
        if (args.length == 1) {
            return pop(args[0], self);
        } else if (args.length == 2) {
            return pop(args[0], args[1], self);
        } else {
            throw new RuntimeException("wrong number of arguments for pop()");
        }
    }
    
    public Object pop(Object arg, Object self) {
        PDictionary dict = (PDictionary) self;
  
        Object retVal = dict.getMap().get(arg);
        if (retVal != null) {
            dict.getMap().remove(arg);
            return retVal;
        } else {
            throw new RuntimeException("invalid key for pop()");
        }
    }
    
    public Object pop(Object arg0, Object arg1, Object self) {
        PDictionary dict = (PDictionary) self;
  
        Object retVal = dict.getMap().get(arg0);
        if (retVal != null) {
            dict.getMap().remove(arg0);
            return retVal;
        } else {
            return arg1;
        }
    }

    @ModuleMethod
    public PList keys(Object[] args, Object self) {
        PDictionary dict = (PDictionary) self;

        if (args.length == 0) {
            return new PList(new ArrayList<Object>(dict.getMap().keySet()));
        } else {
            throw new RuntimeException("wrong number of arguments for keys()");
        }
    }
    
    public PList keys(Object arg, Object self) {
        throw new RuntimeException("wrong number of arguments for keys()");
    }

    public PList keys(Object arg0, Object arg1, Object self) {
        throw new RuntimeException("wrong number of arguments for keys()");
    }
    
    @ModuleMethod
    public PList items(Object[] args, Object self) {
        PDictionary dict = (PDictionary) self;

        if (args.length == 0) {
            return new PList(dict.getMap().entrySet());
        } else {
            throw new RuntimeException("wrong number of arguments for items()");
        }
    }
    
    public PList items(Object arg, Object self) {
        throw new RuntimeException("wrong number of arguments for items()");
    }

    public PList items(Object arg0, Object arg1, Object self) {
        throw new RuntimeException("wrong number of arguments for items()");
    }

    @ModuleMethod
    public boolean hasKey(Object[] args, Object self) {
        if (args.length == 1) {
            return hasKey(args[0], self);
        } else {
            throw new RuntimeException("wrong number of arguments for has_key()");
        }
    }
    
    public boolean hasKey(Object arg, Object self) {
        PDictionary dict = (PDictionary) self;

        return dict.getMap().containsKey(arg);
    }
    
    public boolean hasKey(Object arg0, Object arg1, Object self) {
        throw new RuntimeException("wrong number of arguments for has_key()");
    }

    @ModuleMethod
    public Object get(Object[] args, Object self) {
        PDictionary dict = (PDictionary) self;

        if (args.length == 1) {
            return dict.getMap().get(args[0]);
        } else if (args.length == 2) {
            if (dict.getMap().get(args[0]) != null) {
                return dict.getMap().get(args[0]);
            } else {
                return args[1];
            }
        } else {
            throw new RuntimeException("wrong number of arguments for get()");
        }
    }
    
    public Object get(Object arg, Object self) {
        PDictionary dict = (PDictionary) self;
        
        return dict.getMap().get(arg);
    }
    
    public Object get(Object arg0, Object arg1, Object self) {
        PDictionary dict = (PDictionary) self;

        if (dict.getMap().get(arg0) != null) {
            return dict.getMap().get(arg0);
        } else {
            return arg1;
        }
    }

    @ModuleMethod
    public PDictionary copy(Object[] args, Object self) {
        PDictionary dict = (PDictionary) self;

        if (args.length == 0) {
            return new PDictionary(dict.getMap());
        } else {
            throw new RuntimeException("wrong number of arguments for copy()");
        }
    }
    
    public PDictionary copy(Object arg, Object self) {
        throw new RuntimeException("wrong number of arguments for copy()");
    }
    
    public PDictionary copy(Object arg0, Object arg1, Object self) {
        throw new RuntimeException("wrong number of arguments for copy()");
    }


    @ModuleMethod
    public PDictionary clear(Object[] args, Object self) {
        PDictionary dict = (PDictionary) self;

        if (args.length == 0) {
            dict.getMap().clear();
            return dict;
        } else {
            throw new RuntimeException("wrong number of arguments for clear()");
        }
    }
    
    public PDictionary clear(Object arg, Object self) {
        throw new RuntimeException("wrong number of arguments for clear()");
    }
    
    public PDictionary clear(Object arg0, Object arg1, Object self) {
        throw new RuntimeException("wrong number of arguments for clear()");
    }

    @ModuleMethod
    public PList values(Object[] args, Object self) {
        PDictionary dict = (PDictionary) self;

        if (args.length == 0) {
            return new PList(new ArrayList<Object>(dict.getMap().values()));
        } else {
            throw new RuntimeException("wrong number of arguments for values()");
        }
    }
    
    public PDictionary values(Object arg, Object self) {
        throw new RuntimeException("wrong number of arguments for values()");
    }
    
    public PDictionary values(Object arg0, Object arg1, Object self) {
        throw new RuntimeException("wrong number of arguments for values()");
    }
}
