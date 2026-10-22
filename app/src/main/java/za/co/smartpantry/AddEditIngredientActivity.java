package za.co.smartpantry;
import android.os.Bundle;import android.widget.*;import androidx.appcompat.app.AppCompatActivity;
public class AddEditIngredientActivity extends AppCompatActivity{DatabaseHelper
        db;long id=-1;EditText name,quantity,unit,expiry;
        @Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_add_edit);
            db=new DatabaseHelper(this);name=findViewById(R.id.nameInput);
            quantity=findViewById(R.id.quantityInput);
            unit=findViewById(R.id.unitInput);
            expiry=findViewById(R.id.expiryInput);
            id=getIntent().getLongExtra("id",-1);
            if(id>0){Ingredient i=db.getIngredient(id);
                if(i!=null){((TextView)findViewById(R.id.formTitle)).setText("Edit ingredient");
                    name.setText(i.name);
                    quantity.setText(String.valueOf(i.quantity));
                    unit.setText(i.unit);expiry.setText(i.expiry);}}findViewById(R.id.saveButton).setOnClickListener(v->save());
        }
        private void save(){String n=name.getText().toString().trim(),q=quantity.getText().toString().trim(),u=unit.getText().toString().trim(),e=expiry.getText().toString().trim();
            if(n.isEmpty()||q.isEmpty()||u.isEmpty()){Toast.makeText(this,"Enter an ingredient name, quantity and unit.",Toast.LENGTH_LONG).show();
                return;
            }
            double amount;try{amount=Double.parseDouble(q);if(!Double.isFinite(amount)||amount<=0)throw new NumberFormatException();
            }
            catch(NumberFormatException ex){quantity.setError("Enter a quantity greater than zero");return;
            }
            if(!e.isEmpty()&&!e.matches("\\d{4}-\\d{2}-\\d{2}")){expiry.setError("Use YYYY-MM-DD format");return;
            }
            long saved=db.saveIngredient(new Ingredient(id,n,amount,u,e));
                if(saved<0)Toast.makeText(this,"Could not save ingredient.",Toast.LENGTH_SHORT).show();else{Toast.makeText(this,"Ingredient saved",Toast.LENGTH_SHORT).show();finish();
                }
        }
}
